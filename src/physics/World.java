package physics;

import static utils.Utils.nearZero;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.random.RandomGenerator;

import cell.Cell;
import cell.Substance;
import cell.WorldSettings;
import display.DrawContext;
import mutation.MutationGenerator;
import utils.UnorderedPair;
	
public class World {
	private int width, height;
	private List<BallEntity> entities;
	private List<Cell> cells;
	private List<AbstractWall> walls;
	private List<Cell> newCells;
	public final WorldSettings settings;
	public final MutationGenerator mutationGenerator;
	public Cell selectedCell; // may want to go to private when not debugging
	private CellPosCache cache;
	public final RandomGenerator rng = RandomGenerator.getDefault(); // TODO maybe give options to specify rng

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public World(int width, int height, WorldSettings settings) {
		this.width = width;
		this.height = height;
		this.entities = new LinkedList<BallEntity>();
		this.cells = new LinkedList<Cell>();
		this.newCells = new LinkedList<Cell>();
		this.walls = new LinkedList<AbstractWall>();
		this.settings = settings;
		this.mutationGenerator = new MutationGenerator();
		this.cache = new CellPosCache(25);
	}
	
	public void addCell(Cell cell) {
		this.addEntity(cell);
		this.cells.add(cell);
	}
	
	public List<Cell> getCells() {
		return this.cells;
	}
	
	public void addEntity(BallEntity cell) {
		this.entities.add(cell);
		this.cache.add(cell);
	}
	
	public void addWall(AbstractWall wall) {
		this.walls.add(wall);
	}
	
	public void queueCell(Cell cell) {
		this.newCells.add(cell);
	}
	
	public void computeForces() {
		// Awaiting real implementation
		
		// Zero all acceleration accumulators
		for (BallEntity a : entities) {
			a.tickAcc.zero();
		}
		
		// Add ball-ball collision accelerations
		HashSet<UnorderedPair<BallEntity>> collisions = new HashSet<>();
		cache.clear();
		for (BallEntity a : entities) {
			cache.add(a);
		}
		for (BallEntity a : entities) {
			cache.radiusQuery(a, a.radius() * 2).filter(b -> Vector.dist(a.pos(), b.pos()) < a.radius() + b.radius()).forEach(b -> collisions.add(new UnorderedPair<>(a, b)));
		}
		for (UnorderedPair<BallEntity> x : collisions) {
			BallEntity a = x.a;
			BallEntity b = x.b;
			double d = Vector.dist(a.pos(), b.pos());
			Vector force = Vector.sub(a.pos(), b.pos()).normalize().mult(settings.getCollisionFactor() * (a.radius() + b.radius() - d));
			a.tickAcc.add(force);
			b.tickAcc.add(force.mult(-1));
		}
//		for (BallEntity a : entities) {
//			for (BallEntity b : entities) {
//				double d = Vector.dist(a.pos(), b.pos());
//				if (d < a.radius() + b.radius() && a != b) {
//					Vector force = Vector.sub(a.pos(), b.pos()).normalize().mult(settings.getCollisionFactor() * (a.radius() + b.radius() - d));
//					a.tickAcc.add(force);
//				}
//			}
//		}
		
		// Add ball-wall collision accelerations
		for (AbstractWall w : walls) {
			for (BallEntity a : entities) {
				double d = Vector.dist(a.pos, w.getCenter());
				if (d < a.radius() + w.getEffectiveRadius()) {
					// Effective "collision"
					Vector relative = a.pos.clone().sub(w.getCenter());
					Vector force = w.getForce(relative, a.radius()).mult(settings.getCollisionFactor());
					a.tickAcc.add(force);
				}
			}
		}
		
		// Add ball-edge collisions. Currently these are always on but they might become toggleable later
		for (BallEntity a : entities) {
			if (a.pos.x < a.radius()) {
				a.tickAcc.add(new Vector(a.radius() - a.pos.x, 0).mult(settings.getCollisionFactor()));
			}
			if (this.width - a.pos.x < a.radius()) {
				a.tickAcc.add(new Vector(this.width - a.pos.x - a.radius(), 0).mult(settings.getCollisionFactor()));
			}
			if (a.pos.y < a.radius()) {
				a.tickAcc.add(new Vector(0, a.radius() - a.pos.y).mult(settings.getCollisionFactor()));
			}
			if (this.height - a.pos.y < a.radius()) {
				a.tickAcc.add(new Vector(0, this.width - a.pos.y - a.radius()).mult(settings.getCollisionFactor()));
			}
		}
		
		// Add drag
		for (BallEntity a : entities) {
			a.tickAcc.add(Vector.mult(a.vel, -settings.getDragFactor() * a.radius()));
		}
		
		// Cells accelerate
		for (Cell c : cells) {
			c.tickAcc.add(c.moveAcc().mult(settings.getAccelFactor()));
		}
		
		// Brownian motion
		for (BallEntity a : entities) {
			a.tickAcc.add(new Vector(rng.nextGaussian(), rng.nextGaussian()));
		}
		
		// Divide through by masses
		for (BallEntity a : entities) {
			a.tickAcc.mult(1 / a.mass());
		}
	}
	
	public void tick() {		
		/* COMPUTE STEP */
		
		// Computational tick
		for (BallEntity e : entities) {
			e.tick();
		}
		
		/* CREATION STEP */
		
		// Add new cells to relevant lists
		entities.addAll(newCells);
		cells.addAll(newCells);
		newCells.clear();
		
		/* ENERGY STEP */
		
		// Add and subtract energy, kill cells that run out of energy or other resource
		for (Cell e : cells) {
			// TODO resolve order of operations with energy flow! What I actually want to happen is that cell energies are only clamped at the end
			double photo = Math.min(e.getSubstance(Substance.CHLOROPHYLL.id), lightAtPoint(e.pos())) * settings.getPhotoEnergy();
			e.addSubstance(Substance.NRG.id, photo-e.costs());
			if (nearZero(e.nrg()) || nearZero(e.body())) {
				e.kill();
			}
		}

		// Remove killed cells
		entities.removeIf(obj -> obj.isDead());
		cells.removeIf(obj -> obj.isDead());
		
		/* MOVEMENT STEP */
		
		// Cells rotate as requested
		for (Cell e : cells) {
			e.setFacing(e.getFacing() + e.rotation());
			e.rotRequest = 0; // TODO should this instead be a method call
		}
		
		// Euler-Cromer algorithm: update velocity, then update position with updated velocity
		computeForces();
		for (BallEntity e : entities) {
			e.lastPos.set(e.pos);
			e.lastVel.set(e.vel);
			e.vel.add(e.tickAcc);
			e.pos.add(e.vel);
		}
		
		// Print debug output
		if (selectedCell != null) {
			if (selectedCell.isDead()) {
//				System.out.println("Selected cell has died.");
				selectedCell = null;
			}
			else {
//				System.out.println(Arrays.toString(selectedCell.substances));
//				System.out.println(selectedCell.getProgram().getStatements()[selectedCell.getI()]);
			}
		}
	}
	
	public void draw(DrawContext c) {
		Graphics g = c.g;
		
		g.clearRect(0, 0, c.toZoom(this.width), c.toZoom(this.height));
		
		// Draw balls
		for (BallEntity e : entities) {
			e.draw(c);
		}
		
		// Draw walls
		for (AbstractWall w : walls) {
			w.draw(c);
		}
		
		// Draw the selection envelope
		if (selectedCell != null) {
			int x = c.toZoom(selectedCell.pos.x);
			int y = c.toZoom(selectedCell.pos.y);
			int r = c.toZoom(selectedCell.radius() * 1.1);
			g.setColor(Color.YELLOW);
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
		}
	}
	
	public Cell cellAtPoint(Vector point) {
		Cell res = null;
		double best = Double.POSITIVE_INFINITY;
		for (Cell c : cells) {
			double d = Vector.dist(c.pos(), point);
			if (d < c.radius() && d < best) {
				best = d;
				res = c;
			}
		}
		return res;
	}
	
	public double lightAtPoint(Vector point) {
		// TODO implement actual light calculations; currently just a point light at the center
		double dx = point.x - width/2.0;
		double dy = point.y - height/2.0;
		double d = Math.sqrt(dx*dx+dy*dy);
		double r = Math.min(width, height) / 2 - 100;
		return Math.max(0, (100 - 4*Math.abs(d - r)) * (width - point.x) / width);
	}

	public void click(double clickX, double clickY, MouseEvent e) {
		Vector point = new Vector(clickX, clickY);
		System.out.println(point + " clicked");
		System.out.println("Light level: " + lightAtPoint(point));
		this.selectedCell = cellAtPoint(point);
		if (selectedCell != null) {
			System.out.println("Cell selected.");
			System.out.println("DNA: " + selectedCell.getDna());
			System.out.println("Program: " + selectedCell.getProgram());
			System.out.println("Substances: " + Arrays.toString(selectedCell.substances));
		}
		else {
			System.out.println("No cell selected.");
		}
	}
}
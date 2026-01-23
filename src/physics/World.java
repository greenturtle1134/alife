package physics;

import java.awt.Graphics;
import java.util.ArrayList;

import cell.Cell;
import cell.CostSettings;
import cell.Substance;
import display.DrawContext;
import utils.Utils;

public class World {
	private int width, height;
	private ArrayList<BallEntity> entities;
	private ArrayList<Cell> cells;
	private ArrayList<AbstractWall> walls;
	public CostSettings costSettings;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public World(int width, int height, CostSettings costSettings) {
		this.width = width;
		this.height = height;
		this.entities = new ArrayList<BallEntity>();
		this.cells = new ArrayList<Cell>();
		this.walls = new ArrayList<AbstractWall>();
		this.costSettings = costSettings;
	}
	
	public void addCell(Cell cell) {
		this.addEntity(cell);
		this.cells.add(cell);
	}
	
	public void addEntity(BallEntity cell) {
		this.entities.add(cell);
	}
	
	public void addWall(AbstractWall wall) {
		this.walls.add(wall);
	}
	
	public void computeForces() {
		// Awaiting real implementation
		
		// Zero all acceleration accumulators
		for (BallEntity a : entities) {
			a.tickAcc.zero();
		}
		
		// Add ball-ball collision accelerations
		for (BallEntity a : entities) {
			for (BallEntity b : entities) {
				double d = Vector.dist(a.pos, b.pos);
				if (a != b && d < a.radius() + b.radius()) {
					// Do a collision
					// Force on a is in direction of (a-b)
					Vector force = Vector.sub(a.pos, b.pos).normalize().mult(0.1 * (a.radius() + b.radius() - d));
					a.tickAcc.add(force);
				}
			}
		}
		
		// Add ball-wall collision accelerations
		for (AbstractWall w : walls) {
			for (BallEntity a : entities) {
				double d = Vector.dist(a.pos, w.getCenter());
				if (d < a.radius() + w.getEffectiveRadius()) {
					// Effective "collision"
					Vector relative = a.pos.clone().sub(w.getCenter());
					Vector force = w.getForce(relative, a.radius()).mult(a.mass());
					a.tickAcc.add(force);
				}
			}
		}
		
		// Add ball-edge collisions. Currently these are always on but they might become toggleable later
		for (BallEntity a : entities) {
			if (a.pos.x < a.radius()) {
				a.tickAcc.add(new Vector(a.radius() - a.pos.x, 0));
			}
			if (this.width - a.pos.x < a.radius()) {
				a.tickAcc.add(new Vector(this.width - a.pos.x - a.radius(), 0));
			}
			if (a.pos.y < a.radius()) {
				a.tickAcc.add(new Vector(0, a.radius() - a.pos.y));
			}
			if (this.height - a.pos.y < a.radius()) {
				a.tickAcc.add(new Vector(0, this.width - a.pos.y - a.radius()));
			}
		}
		
		// Add drag
		for (BallEntity a : entities) {
			a.tickAcc.add(Vector.mult(a.vel, -0.01));
		}
		
		// Cells accelerate
		for (Cell c : cells) {
			c.tickAcc.add(c.moveAcc());
		}
		
		// Divide through by masses
		for (BallEntity a : entities) {
			a.tickAcc.mult(1 / a.mass());
		}
	}
	
	public void tick() {
		// Computational tick
		for (BallEntity e : entities) {
			e.tick();
		}
		
		// Impose costs
		for (Cell e : cells) {
			e.setSubstance(Substance.NRG.id, e.nrg() - e.costs());
		}
		
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
	}
	
	public void draw(DrawContext c) {
		Graphics g = c.getG();
		double zoom = c.getZoom();
		
		g.clearRect(0, 0, Utils.round(this.width * zoom), Utils.round(this.height * zoom));
		
		// Draw balls
		for (BallEntity e : entities) {
			e.draw(c);
		}
		
		// Draw walls
		for (AbstractWall w : walls) {
			w.draw(c);
		}
	}
}
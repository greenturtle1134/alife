package physics;

import java.awt.Graphics;
import java.util.ArrayList;

import cell.Cell;
import display.DrawContext;
import utils.Utils;

public class World {
	private int width, height;
	private ArrayList<BallEntity> entities;
	private ArrayList<Cell> cells;
	private ArrayList<AbstractWall> walls;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.entities = new ArrayList<BallEntity>();
		this.cells = new ArrayList<Cell>();
		this.walls = new ArrayList<AbstractWall>();
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
					Vector relative = a.pos.copy().sub(w.getCenter());
					Vector force = w.getForce(relative, a.radius());
					a.tickAcc.add(force);
				}
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
		// Tick step
		for (BallEntity e : entities) {
			e.tick();
		}
		
		/*
		// Euler-Richardson algorithm rejected
		
		// Place first estimate into pos and vel, buffer initial position
		computeForces();
		for (Ball e : entities) {
			e.lastPos.set(e.pos);
			e.lastVel.set(e.vel);
			e.pos.add(Vector.mult(e.vel, 0.5));
			e.vel.add(Vector.mult(e.tickAcc, 0.5));
		}
		
		// Make second estimate based on first estimate
		computeForces();
		
		// Reset to initial position and use second estimate
		for (Ball e : entities) {
			e.pos.set(e.lastPos);
			e.vel.set(e.lastVel);
			e.pos.add(e.vel);
			e.vel.add(e.tickAcc);
		}
		*/
		
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
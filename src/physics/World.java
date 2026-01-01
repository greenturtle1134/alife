package physics;

import java.awt.Graphics;
import java.util.ArrayList;

import utils.Utils;

public class World {
	private int width, height;
	private ArrayList<TestBall> entities;
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
		this.entities = new ArrayList<TestBall>();
		this.walls = new ArrayList<AbstractWall>();
	}
	
	public void addEntity(TestBall cell) {
		this.entities.add(cell);
	}
	
	public void addWall(AbstractWall wall) {
		this.walls.add(wall);
	}
	
	public void computeForces() {
		// Awaiting real implementation
		
		// Zero all acceleration accumulators
		for (TestBall a : entities) {
			a.tickAcc.zero();
		}
		
		// Add ball-ball collision accelerations
		for (TestBall a : entities) {
			for (TestBall b : entities) {
				double d = Vector.dist(a.pos, b.pos);
				if (a != b && d < a.radius + b.radius) {
					// Do a collision
					// Force on a is in direction of (a-b)
					Vector force = Vector.sub(a.pos, b.pos).normalize().mult(0.1 * (a.radius + b.radius - d));
					a.tickAcc.add(force);
				}
			}
		}
		
		// Add ball-wall collision accelerations
		for (AbstractWall w : walls) {
			for (TestBall a : entities) {
				double d = Vector.dist(a.pos, w.getCenter());
				if (d < a.radius + w.getEffectiveRadius()) {
					// Effective "collision"
					Vector relative = a.pos.copy().sub(w.getCenter());
					Vector force = w.getForce(relative, a.radius);
					a.tickAcc.add(force);
				}
			}
		}
		
		// Add drag
		for (TestBall a : entities) {
			a.tickAcc.add(Vector.mult(a.vel, -0.01));
		}
		
		// Divide through by masses
		for (TestBall a : entities) {
			a.tickAcc.mult(1 / a.mass());
		}
	}
	
	public void tick() {
		// Tick step
		for (TestBall e : entities) {
			e.tick();
		}
		
//		// Euler-Richardson algorithm
		// Tested, but found to not conserve energies
//		
//		// Place first estimate into pos and vel, buffer initial position
//		computeForces();
//		for (Ball e : entities) {
//			e.lastPos.set(e.pos);
//			e.lastVel.set(e.vel);
//			e.pos.add(Vector.mult(e.vel, 0.5));
//			e.vel.add(Vector.mult(e.tickAcc, 0.5));
//		}
//		
//		// Make second estimate based on first estimate
//		computeForces();
//		
//		// Reset to initial position and use second estimate
//		for (Ball e : entities) {
//			e.pos.set(e.lastPos);
//			e.vel.set(e.lastVel);
//			e.pos.add(e.vel);
//			e.vel.add(e.tickAcc);
//		}
		
		// Euler-Cromer algorithm: update velocity, then update position with updated velocity
		computeForces();
		for (TestBall e : entities) {
			e.lastPos.set(e.pos);
			e.lastVel.set(e.vel);
			e.vel.add(e.tickAcc);
			e.pos.add(e.vel);
		} 
	}
	
	public void draw(Graphics g, double zoom) {
		g.clearRect(0, 0, Utils.round(this.width * zoom), Utils.round(this.height * zoom));
		
		// Draw balls
		for (TestBall e : entities) {
			e.draw(g, zoom);
		}
		
		// Draw walls
		for (AbstractWall w : walls) {
			w.draw(g, zoom);
		}
	}
}
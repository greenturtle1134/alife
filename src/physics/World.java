package physics;

import java.util.ArrayList;

public class World {
	private int width, height;
	private ArrayList<Ball> entities;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.entities = new ArrayList<Ball>();
	}
	
	public void addEntity(Ball cell) {
		this.entities.add(cell);
	}
	
	public void computeForces() {
		// Awaiting real implementation
		
		// Zero all acceleration accumulators
		for (Ball a : entities) {
			a.tickAcc.zero();
		}
		
		// Add ball-ball collision accelerations
		for (Ball a : entities) {
			for (Ball b : entities) {
				double d = Vector.dist(a.pos, b.pos);
				if (a != b && d < a.radius + b.radius) {
					// Do a collision
					// Force on a is in direction of (a-b)
					Vector force = Vector.sub(a.pos, b.pos).normalize().mult(0.1 * (a.radius + b.radius - d));
					a.tickAcc.add(force);
				}
			}
		}
	}
	
	public void tick() {
		// Tick step
		for (Ball e : entities) {
			e.tick();
		}
		
//		// Euler-Richardson algorithm
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
		for (Ball e : entities) {
			e.lastPos.set(e.pos);
			e.lastVel.set(e.vel);
			e.vel.add(e.tickAcc);
			e.pos.add(e.vel);
		} 
	}
}
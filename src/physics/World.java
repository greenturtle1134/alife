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
	
	public void tick() {
		// Awaiting real implementation
		for (Ball a : entities) {
			for (Ball b : entities) {
				double d = Vector.dist(a.pos, b.pos);
				if (a != b && d < a.radius + b.radius) {
					// Do a collision
					// Force on a is in direction of (a-b)
					Vector force = Vector.sub(a.pos, b.pos).normalize().mult(0.1);
					a.tickAcc.add(force);
				}
			}
		}
		
		// Tick step
		for (Ball e : entities) {
			e.tick();
		}
		
		// Move step
		for (Ball e : entities) {
			e.move();
		}
	}
}
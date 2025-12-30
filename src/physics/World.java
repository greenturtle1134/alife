package physics;

import java.util.ArrayList;

public class World {
	private int width, height;
	private ArrayList<PhysicsEntity> entities;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.entities = new ArrayList<PhysicsEntity>();
	}
	
	public void addEntity(PhysicsEntity cell) {
		this.entities.add(cell);
	}
	
	public void tick() {
		// Awaiting real implementation
		for (PhysicsEntity e : entities) {
			e.tickAcc.y -= 9.8;
		}
		
		// Tick step
		for (PhysicsEntity e : entities) {
			e.tick();
		}
		
		// Move step
		for (PhysicsEntity e : entities) {
			e.move();
		}
	}
}
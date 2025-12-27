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
//		for (Cell c : cells) {
//			c.tick();
//			// TODO: very simple friction model
//			c.addForce(c.getDx()*-0.1, c.getDy()*-0.1);
//		}
//		
//		for (Cell c : cells) {
//			c.move();
//		}
	}
}
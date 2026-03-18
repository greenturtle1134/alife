package physics;

import cell.Cell;
import display.Drawable;

/**
 * A Particle is a point entity that travels in straight lines and interacts with cells it contacts.
 * A field createdTime is provided to facilitate
 * 
 */
public abstract class Particle extends Entity implements Positionable, Drawable {
	public Vector pos;
	public Vector vel;
	public long createdTime;
	public Entity owner;
	
	public Particle(World world, Vector pos, Vector vel, Entity owner, long createdTime) {
		super(world);
		this.pos = pos;
		this.vel = vel;
		this.owner = owner;
		this.createdTime = createdTime;
	}
	
	public abstract boolean interact(Cell e);
	
	@Override
	public Vector pos() {
		return pos;
	}

//	@Override
//	public void tick() {
//		this.pos.add(this.vel);
//		if (pos.x < 0) {
//			pos.x = -pos.x;
//			vel.x *= -1;
//		}
//		if (pos.x > world.getWidth()) {
//			pos.x = 2*world.getWidth() - pos.x;
//			vel.x *= -1;
//		}
//		if (pos.y < 0) {
//			pos.y = -pos.y;
//			vel.y *= -1;
//		}
//		if (pos.y > world.getHeight()) {
//			pos.y = 2*world.getHeight() - pos.y;
//			vel.y *= -1;
//		}
//	}

}

package physics;

import display.Drawable;

/**
 * Represents an entity with position, velocity, and mass.
 * Such entities will be stepped by the World applying Newton's second law.
 * This class exists to facilitate the remote future possibility of non-ball-shaped physics objects,
 * currently all the code is set up just for balls.
 */
public abstract class PhysicsEntity implements Drawable {
	protected World world;
	protected Vector pos;
	protected Vector vel;
	protected Vector lastPos;
	protected Vector lastVel;
	protected Vector tickAcc;
	
	// Dead param; being dead means it should be deleted from all lists immediately
	private boolean dead;

	public PhysicsEntity(World world, Vector pos, Vector vel) {
		this.world = world;
		this.pos = pos;
		this.vel = vel;
		this.lastPos = pos;
		this.lastVel = vel;
		this.tickAcc = Vector.getZeroVector();
	}
	
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Vector pos() {
		return pos;
	}

	public Vector vel() {
		return vel;
	}

	public Vector tickAcc() {
		return tickAcc;
	}
	
	public void kill() {
		dead = true;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public abstract double mass();
	
	public abstract void tick();
}

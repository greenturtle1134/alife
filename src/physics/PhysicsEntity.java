package physics;

import display.Drawable;

/**
 * Represents an entity with position, velocity, and mass.
 * Such entities will be stepped by the World applying Newton's second law.
 * This class exists to facilitate the remote future possibility of non-ball-shaped physics objects,
 * currently all the code is set up just for balls.
 */
public abstract class PhysicsEntity extends Entity implements Drawable, Positionable {
	protected Vector pos;
	protected Vector vel;
	protected Vector lastPos;
	protected Vector lastVel;
	protected Vector tickAcc;
	
	public PhysicsEntity(World world, Vector pos, Vector vel) {
		super();
		this.world = world;
		this.pos = pos;
		this.vel = vel;
		this.lastPos = pos;
		this.lastVel = vel;
		this.tickAcc = Vector.getZeroVector();
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
	
	public abstract double mass();
}

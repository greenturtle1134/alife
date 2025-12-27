package physics;

public abstract class PhysicsEntity {
	protected World world;
	protected Vector pos;
	protected Vector vel;
	protected Vector tickAcc;

	public PhysicsEntity(World world, Vector pos, Vector vel) {
		this.world = world;
		this.pos = pos;
		this.vel = vel;
		this.tickAcc = Vector.zero();
	}

	public World getWorld() {
		return world;
	}

	public Vector getPos() {
		return pos;
	}

	public Vector getVel() {
		return vel;
	}

	public Vector getTickAcc() {
		return tickAcc;
	}
}

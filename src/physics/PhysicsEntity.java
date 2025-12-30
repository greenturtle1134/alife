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
		this.tickAcc = Vector.getZeroVector();
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Vector getPos() {
		return pos;
	}

	public void setPos(Vector pos) {
		this.pos = pos;
	}

	public Vector getVel() {
		return vel;
	}

	public void setVel(Vector vel) {
		this.vel = vel;
	}

	public Vector getTickAcc() {
		return tickAcc;
	}

	public void setTickAcc(Vector tickAcc) {
		this.tickAcc = tickAcc;
	}
	
	public abstract void tick();
	
	public void move() {
		this.pos.add(this.vel);
		this.vel.add(this.tickAcc);
		this.tickAcc.zero();
	}
}

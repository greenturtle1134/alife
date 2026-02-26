package physics;

/**
 * Represents absolutely anything which exists in a World
 *
 * Honestly this may not even be necessary right now
 */
public abstract class Entity {

	protected World world;
	private boolean dead;

	public Entity() {
		super(); // TODO should Entity default constructor set the World field?
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public void kill() {
		dead = true;
	}

	public boolean isDead() {
		return dead;
	}

	public abstract void tick();

}
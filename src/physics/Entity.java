package physics;

/**
 * Represents absolutely anything which exists in a World.
 * The core functionality is a field for a world and a boolean flag for whether the entity is dead, with a kill() method to flip this flag.
 */
public abstract class Entity {

	protected World world;
	private boolean dead;

	public Entity(World world) {
		this.world = world;
		this.dead = false;
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
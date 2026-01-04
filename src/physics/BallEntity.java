package physics;

/**
 * Represents an entity with position, velocity, mass, and radius.
 * This radius is used by the World to compute collision and can be manipulated by subclasses.
 * This will include cells and other such things.
 */
public abstract class BallEntity extends PhysicsEntity {

	public BallEntity(World world, Vector pos, Vector vel) {
		super(world, pos, vel);
	}
	
	public abstract double radius();

}

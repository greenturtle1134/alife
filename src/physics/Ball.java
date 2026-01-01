package physics;

import java.awt.Graphics;

import utils.Utils;

/**
 * Represents an entity that additionally has a radius.
 * This radius is used by the World to compute collision.
 * This will include cells and other such things.
 */
// Currently a concrete class for testing but will become abstract in the final version
public class Ball extends PhysicsEntity {
	
	protected double radius;

	public Ball(World world, Vector pos, Vector vel, double radius) {
		super(world, pos, vel);
		this.radius = radius;
	}
	
	@Override
	public void tick() {
		// do nothing
	}
	
	@Override
	public void draw(Graphics g, double zoom) {
		int x = Utils.round(this.pos.x * zoom);
		int y = Utils.round(this.pos.y * zoom);
		int r = Utils.round(this.radius * zoom);
		g.drawOval(x - r, y - r, 2 * r, 2 * r);
	}

}

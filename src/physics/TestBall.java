package physics;

import java.awt.Graphics;

import static utils.Utils.round;


/**
 * A blank Ball implementation for testing
 */
public class TestBall extends BallEntity {
	
	private double mass;
	
	public TestBall(World world, Vector pos, Vector vel, double radius, double mass) {
		super(world, pos, vel, radius);
		this.mass = mass;
	}

	@Override
	public void tick() {
		// do nothing
	}
	
	@Override
	public void draw(Graphics g, double zoom) {
		int x = round(this.pos.x * zoom);
		int y = round(this.pos.y * zoom);
		int r = round(this.radius * zoom);
		g.drawOval(x - r, y - r, 2 * r, 2 * r);
	}

	@Override
	public double mass() {
		return mass;
	}

}

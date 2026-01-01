package physics;

import java.awt.Graphics;

public abstract class AbstractWall {
	public abstract Vector getCenter();
	public abstract double getEffectiveRadius();
	public abstract Vector getForce(Vector relative, double radius);
	public abstract void draw(Graphics g, double zoom);
}

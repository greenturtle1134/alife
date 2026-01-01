package physics;

import display.DrawContext;
import display.Drawable;

public abstract class AbstractWall implements Drawable {
	public abstract Vector getCenter();
	public abstract double getEffectiveRadius();
	public abstract Vector getForce(Vector relative, double radius);
	public abstract void draw(DrawContext c);
}

package physics;

import static utils.Utils.round;

import java.awt.Color;
import java.awt.Graphics;

import display.DrawContext;

public class LineWall extends AbstractWall {
	
	private Vector center;
	private Vector extent;
	private Vector perp;
	private double halfLength;

	public LineWall(double x1, double y1, double x2, double y2) {
		this.center = new Vector((x1 + x2) / 2, (y1 + y2) / 2);
		this.extent = new Vector((x2 - x1) / 2, (y2 - y1) / 2);
		
		// Now precompute required properties
		this.perp = new Vector(y1 - y2, x1 - x2).normalize();
		this.halfLength = this.extent.norm();
	}
	
	@Override
	public Vector getCenter() {
		return this.center;
	}

	@Override
	public double getEffectiveRadius() {
		return halfLength;
	}

	@Override
	public Vector getForce(Vector relative, double radius) {
		double dotExtent = Vector.dot(relative, this.extent) / this.halfLength;
		if (dotExtent > -this.halfLength && dotExtent < this.halfLength) {
			double dotPerp = Vector.dot(relative, this.perp);
			if (dotPerp > -radius && dotPerp < radius) {
				double magn = radius * Math.signum(dotPerp) - dotPerp;
				return Vector.mult(this.perp, magn);
			}
		}
		return Vector.getZeroVector();
	}

	@Override
	public void draw(DrawContext c) {
		Graphics g = c.getG();
		double zoom = c.getZoom();
		
		g.setColor(Color.BLACK);
		
		g.drawLine(
			round((center.x - extent.x) * zoom),
			round((center.y - extent.y) * zoom),
			round((center.x + extent.x) * zoom),
			round((center.y + extent.y) * zoom)
		);
	}

}

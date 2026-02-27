package display;

import static utils.Utils.round;

import java.awt.Graphics;

public class DrawContext {
	public final Graphics g;
	public final double zoom;
	public final int simplifyLimit;
	
	public DrawContext(Graphics g, double zoom, int simplifyLimit) {
		this.g = g;
		this.zoom = zoom;
		this.simplifyLimit = simplifyLimit;
	}

	/**
	 * Converts a double to a pixel coordinate, handling multiplication by zoom and rounding.
	 * @param x - the double to convert
	 * @return corresponding pixel value
	 */
	public int toZoom(double x) {
		return round(x * zoom);
	}
}

package display;

import static utils.Utils.round;

import java.awt.Graphics;

public class DrawContext {
	private Graphics g;
	private double zoom;
	
	public DrawContext(Graphics g, double zoom) {
		this.g = g;
		this.zoom = zoom;
	}

	public Graphics getG() {
		return g;
	}

	public double getZoom() {
		return zoom;
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

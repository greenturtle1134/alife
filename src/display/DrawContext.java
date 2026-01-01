package display;

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
}

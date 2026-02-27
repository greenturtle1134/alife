package display;

import java.awt.Graphics;

import javax.swing.JPanel;

import physics.World;

/**
 * A JPanel that displays a view of a World
 */
public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected World world;
	protected double x;
	protected double y;
	protected double zoom;

	public DisplayPanel(World world, double x, double y, double zoom) {
		super();
		
		this.world = world;
		this.x = x;
		this.y = y;
		this.zoom = zoom;
	}

	public World getWorld() {
		return world;
	}

	public void paintComponent(Graphics g) {
			super.paintComponent(g);
	
			g.translate((int) this.x, (int) this.y);
			DrawContext c = new DrawContext(g, this.zoom, 3);
			world.draw(c);
	}

}
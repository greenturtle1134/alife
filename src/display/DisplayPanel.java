package display;

import java.awt.Graphics;

import javax.swing.JPanel;

import physics.World;

public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private World world;
	int x, y;
	double zoom;

	public DisplayPanel(World world) {
		this(world, 0, 0, 1);
	}
	
	public DisplayPanel(World world, int x, int y, double zoom) {
		super();
		this.world = world;
		this.x = x;
		this.y = y;
		this.zoom = zoom;
	}
	
	public void paintComponent (Graphics g) {
    		super.paintComponent(g);

    		g.translate(this.x, this.y);
    		world.draw(new DrawContext(g, this.zoom));
	}

}

package display;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
		
		this.setFocusable(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double clickX = (e.getX() - x) / zoom;
                double clickY = (e.getY() - y) / zoom;
                if (clickX >= 0 && clickX < world.getWidth() && clickY >= 0 && clickY < world.getHeight()) {
                	world.click(clickX, clickY, e);
                }
                repaint();
            }
        });
	}
	
	public void paintComponent (Graphics g) {
    		super.paintComponent(g);

    		g.translate(this.x, this.y);
    		world.draw(new DrawContext(g, this.zoom));
	}
}

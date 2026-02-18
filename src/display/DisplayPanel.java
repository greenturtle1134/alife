package display;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import cell.WorldSettings;
import physics.World;

public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private World world;
	double x, y;
	double zoom;

	public DisplayPanel(World world) {
		this(world, 0, 0, 1);
	}
	
	public DisplayPanel(World world, double x, double y, double zoom) {
		super();
		this.world = world;
		this.x = x;
		this.y = y;
		this.zoom = zoom;
		
		this.setFocusable(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double clickX = (e.getX() - DisplayPanel.this.x) / DisplayPanel.this.zoom;
                double clickY = (e.getY() - DisplayPanel.this.y) / DisplayPanel.this.zoom;
                if (clickX >= 0 && clickX < world.getWidth() && clickY >= 0 && clickY < world.getHeight()) {
                	world.click(clickX, clickY, e);
                }
                repaint();
            }
        });
        
        this.addMouseWheelListener(new MouseAdapter() {
        	@Override
        	public void mouseWheelMoved(MouseWheelEvent e) {
        		int wheelX = e.getX();
        		int wheelY = e.getY();
        		int scroll = e.getWheelRotation();
        		changeZoom(Math.pow(0.9, scroll), wheelX, wheelY);
        	}
        });
	}
	
	public void paintComponent(Graphics g) {
    		super.paintComponent(g);

    		g.translate((int) this.x, (int) this.y);
    		world.draw(new DrawContext(g, this.zoom));
	}
	
	/**
	 * Zooms by the specified factor on the specified point.
	 * The point is specified in view space, i.e. after the transform.
	 * Also repaints the panel.
	 * @param factor - the factor to zoom by
	 * @param cx - x coordinate of the center point
	 * @param cy - y coordinate of the center point
	 */
	public void changeZoom(double factor, double cx, double cy) {
		this.x = cx + (this.x-cx) * factor;
		this.y = cy + (this.y-cy) * factor;
		this.zoom *= factor;
//		System.out.println("Zoomed to " + this.zoom + "x on " + this.x + ", " + this.y);
		repaint();
	}
	
	/**
	 * Automatically adjusts the zoom level to fit the current size of this panel and centers the view.
	 * Also repaints the panel.
	 */
	public void zoomFit() {
		this.zoom = Math.min((double) this.getHeight() / world.getHeight(), (double) this.getWidth() / world.getWidth());
//		System.out.println("Zoomed to " + this.zoom + "x on " + this.x + ", " + this.y);
		this.x = (this.getWidth() - world.getWidth() * zoom) / 2;
		this.y = (this.getHeight() - world.getHeight() * zoom) / 2;
		repaint();
	}
}

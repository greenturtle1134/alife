package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

import javax.swing.JPanel;

import cell.Cell;
import physics.Vector;
import physics.World;

public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private World world;
	private double x, y;
	private double zoom;
	private PanelListener listener;
	public Cell selectedCell;

	public DisplayPanel(World world) {
		this(world, 0, 0, 1);
	}
	
	public DisplayPanel(World world, double x, double y, double zoom) {
		super();
		this.world = world;
		this.x = x;
		this.y = y;
		this.zoom = zoom;
		this.listener = PanelListener.NULL;
		
		this.setFocusable(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double clickX = (e.getX() - DisplayPanel.this.x) / DisplayPanel.this.zoom;
                double clickY = (e.getY() - DisplayPanel.this.y) / DisplayPanel.this.zoom;
                if (clickX >= 0 && clickX < world.getWidth() && clickY >= 0 && clickY < world.getHeight()) {
            		Vector point = new Vector(clickX, clickY);
            		System.out.println(point + " clicked");
            		System.out.println("Light level: " + world.lightAtPoint(point));
            		selectedCell = world.cellAtPoint(point);
            		if (selectedCell != null) {
            			System.out.println("Cell selected.");
            			System.out.println("DNA: " + selectedCell.getDna());
            			System.out.println("Program: " + selectedCell.getProgram());
            			System.out.println("Substances: " + Arrays.toString(selectedCell.substances));
            		}
            		else {
            			System.out.println("No cell selected.");
            		}
                	listener.cellClicked(selectedCell);
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
	
	public World getWorld() {
		return world;
	}

	public void paintComponent(Graphics g) {
    		super.paintComponent(g);

    		g.translate((int) this.x, (int) this.y);
    		DrawContext c = new DrawContext(g, this.zoom);
    		world.draw(c);
    		
    		// Draw the selection envelope
    		if (selectedCell != null) {
    			int x = c.toZoom(selectedCell.pos().x);
    			int y = c.toZoom(selectedCell.pos().y);
    			int r = c.toZoom(selectedCell.radius() * 1.1);
    			g.setColor(Color.YELLOW);
    			g.drawOval(x - r, y - r, 2 * r, 2 * r);
    		}
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
		listener.setStatus("Zoomed to " + String.format("%.2f", this.zoom) + "x.");
		repaint();
	}
	
	/**
	 * Automatically adjusts the zoom level to fit the current size of this panel and centers the view.
	 * Also repaints the panel.
	 */
	public void zoomFit() {
		this.zoom = Math.min((double) this.getHeight() / world.getHeight(), (double) this.getWidth() / world.getWidth());
		listener.setStatus("Reset zoom to " + String.format("%.2f", this.zoom) + "x.");
		this.x = (this.getWidth() - world.getWidth() * zoom) / 2;
		this.y = (this.getHeight() - world.getHeight() * zoom) / 2;
		repaint();
	}
	
	/**
	 * Sets the Panel to report to a particular listener object.
	 * This method is provided because due to the order I create elements, the status will probably not be ready when I create the panel.
	 * @param listener - listener to attach
	 */
	public void setListener(PanelListener listener) {
		this.listener = listener;
	}
}

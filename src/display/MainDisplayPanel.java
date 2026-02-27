package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

import cell.Cell;
import physics.Vector;
import physics.World;

/**
 * Extends DisplayPanel with functionality for clicking and cell selection, including support for a PanelListener to connect it to an Application.
 * Meant to be used as the central panel of the simulator.
 */
public class MainDisplayPanel extends DisplayPanel {
	private static final long serialVersionUID = 1L;
	
	private PanelListener listener;
	public Cell selectedCell;

	public MainDisplayPanel(World world) {
		this(world, 0, 0, 1);
	}
	
	public MainDisplayPanel(World world, double x, double y, double zoom) {
		super(world, x, y, zoom);
		this.listener = PanelListener.NULL;

		this.setFocusable(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double clickX = (e.getX() - MainDisplayPanel.this.x) / MainDisplayPanel.this.zoom;
                double clickY = (e.getY() - MainDisplayPanel.this.y) / MainDisplayPanel.this.zoom;
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
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		DrawContext c = new DrawContext(g, this.zoom, 3);
		
		// Draw the selection envelope
		if (selectedCell != null) {
			int x = c.toZoom(selectedCell.pos().x);
			int y = c.toZoom(selectedCell.pos().y);
			int r = c.toZoom(selectedCell.radius()) + 2;
			g.setColor(Color.YELLOW);
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
		}
	}
}

package display;

import java.awt.Dimension;

import javax.swing.JFrame;

import physics.World;

public class Application {
	private World world;
	private JFrame frame;
	private DisplayPanel panel;
	private int targetFrameMillis, targetFrameTicks;
	
	public Application(World world, String title, double zoom, int targetFrameMillis, int targetFrameTicks) {
		this.world = world;
		this.frame = new JFrame(title);
		this.panel = new DisplayPanel(world, 0, 0, zoom);
		this.targetFrameMillis = targetFrameMillis;
		this.targetFrameTicks = targetFrameTicks;
		
        frame.add(panel); //adds DisplayGraphics to the frame for viewing
        panel.setPreferredSize(new Dimension((int) (world.getHeight()*panel.zoom), (int) (world.getWidth()*panel.zoom)));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	public void run() {
		new javax.swing.Timer(targetFrameTicks, e -> {
		    for (int i = 0; i<10; i++) {
		    	world.tick();
		    }
		    frame.repaint();
		}).start();
	}
	
	public double getZoom() {
		return panel.zoom;
	}
	
	public void setZoom(double z) {
		panel.zoom = z;
	}
}

package display;

import java.awt.Dimension;

import javax.swing.JFrame;

import physics.World;

public class Application {
	private World world;
	private JFrame frame;
	private DisplayPanel panel;
	private long targetFrameMillis;
	
	public Application(World world, String title, double zoom, long targetFrameMillis) {
		this.world = world;
		this.frame = new JFrame(title);
		this.panel = new DisplayPanel(world, 0, 0, zoom);
		this.targetFrameMillis = targetFrameMillis;
		
        frame.add(panel); //adds DisplayGraphics to the frame for viewing
        panel.setPreferredSize(new Dimension((int) (world.getHeight()*panel.zoom), (int) (world.getWidth()*panel.zoom)));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	public void run() {
		while (true) {
			long start = System.nanoTime();
			frame.repaint();
			world.tick();
			long tickTime = System.nanoTime() - start;
			System.out.println(tickTime);
			try {
				Thread.sleep(targetFrameMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public double getZoom() {
		return panel.zoom;
	}
	
	public void setZoom(double z) {
		panel.zoom = z;
	}
}

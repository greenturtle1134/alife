package display;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.Timer;

import physics.World;

public class Application {
	private World world;
	private JFrame frame;
	private DisplayPanel panel;
	private double zoom;
	private int targetFrameMillis, targetFrameTicks;
	private int t;
	private Timer loop;
	
	public Application(World world, String title, double zoom, int targetFrameMillis, int targetFrameTicks) {
		this.world = world;
		this.zoom = zoom;
		this.frame = new JFrame(title);
		this.panel = new DisplayPanel(world, 0, 0, zoom);
		this.targetFrameMillis = targetFrameMillis;
		this.targetFrameTicks = targetFrameTicks;
		this.t = 0;
		
        frame.add(panel); //adds DisplayGraphics to the frame for viewing
        panel.setPreferredSize(new Dimension((int) (world.getHeight()*panel.zoom), (int) (world.getWidth()*panel.zoom)));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        this.loop = new Timer(targetFrameMillis, e -> {
		    for (int i = 0; i<this.targetFrameTicks; i++) {
		    	world.tick();
		    }
		    frame.repaint();
		});
        
        this.loop.start();
	}
	
//	public void runSaveVideo(String path) {
//		Path dir = Paths.get(path);
//		try {
//			Files.createDirectories(dir);
//			Files.walk(dir).filter(p -> !p.equals(dir)).forEach(f -> {
//				try {
//					Files.delete(f);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				});
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		new javax.swing.Timer(targetFrameMillis, e -> {
//		    for (int i = 0; i<targetFrameTicks; i++) {
//		    	world.tick();
//		    }
//		    frame.repaint();
//		    
//		    BufferedImage image = new BufferedImage((int) (world.getWidth()*zoom), (int) (world.getHeight()*zoom), BufferedImage.TYPE_INT_RGB);
//		    Graphics2D g = image.createGraphics();
//		    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//			g.setBackground(Color.WHITE);
//		    world.draw(new DrawContext(g, zoom));
//		    g.dispose();
//		    Path framePath = dir.resolve(String.format("frame_%05d.png", t));
//		    try {
//				ImageIO.write(image, "png", framePath.toFile());
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		    t++;
//		}).start();
//	}
	
	public double getZoom() {
		return panel.zoom;
	}
	
	public void setZoom(double z) {
		panel.zoom = z;
	}
}

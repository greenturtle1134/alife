package display;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import physics.World;

public class Application {
	private World world;
	private JFrame frame;
	private DisplayPanel panel;
	private int targetFrameMillis, targetFrameTicks;
	private int t;
	private Timer loop;
	private JLabel statusLabel;
	
	public Application(World world, String title, double zoom, int targetFrameMillis, int targetFrameTicks) {
		this.world = world;
		this.frame = new JFrame(title);
		this.panel = new DisplayPanel(world, 0, 0, zoom);
		this.targetFrameMillis = targetFrameMillis;
		this.targetFrameTicks = targetFrameTicks;
		this.t = 0;
		
        frame.add(panel, BorderLayout.CENTER); //adds DisplayGraphics to the frame for viewing
        panel.setPreferredSize(new Dimension(400, 400));
        
		JPanel settingsPanel = new JPanel();
		JButton startButton = new JButton("Start / Stop");
		startButton.addActionListener(e -> {
			if (this.loop.isRunning()) {
				this.loop.stop();
				setStatus("Paused simulation.");
			}
			else {
				this.loop.start();
				setStatus("Started simulation.");
			}
		});
		settingsPanel.add(startButton);
		JButton centerButton = new JButton("Recenter");
		centerButton.addActionListener(e -> {
			panel.zoomFit();
		});
		settingsPanel.add(centerButton);
		frame.add(settingsPanel, BorderLayout.PAGE_START);
		
		JPanel statusPanel = new JPanel();
		statusLabel = new JLabel("Starting...");
		statusPanel.add(statusLabel);
		JLabel fpsLabel = new JLabel("ms/frame: ");
		statusPanel.add(fpsLabel);
		JLabel countLabel = new JLabel("cells: ");
		statusPanel.add(countLabel);
		frame.add(statusPanel, BorderLayout.PAGE_END);
        
        frame.pack();
        panel.zoomFit();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        this.loop = new Timer(targetFrameMillis, e -> {
        	long start = System.currentTimeMillis();
		    for (int i = 0; i<this.targetFrameTicks; i++) {
		    	world.tick();
		    }
		    long end = System.currentTimeMillis();
		    fpsLabel.setText("ms/frame: " + (end - start));
		    countLabel.setText("cells: " + world.getCells().size());
		    frame.repaint();
		});
        
        setStatus("Ready to start.");
	}
	
	public void setStatus(String s) {
		statusLabel.setText(s);
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

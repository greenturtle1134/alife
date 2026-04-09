package display;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import cell.Cell;
import cell.Substance;
import physics.World;

public class Application implements PanelListener {
	private static final double AVERAGE_DECAY_FACTOR = 0.1;
	
	private World world;

	private JFrame frame;
	private MainDisplayPanel panel;
	private JLabel statusLabel;
	private CellInfoPanel cellInfoPanel;
	
	private Timer loop;
	
	private int targetFrameMillis, targetFrameTicks;
	private boolean autoFrameTicks;
	private int t;
	
	private long lastFrame;
	private double averageFrameTime;
	
	public int getTargetFrameMillis() {
		return targetFrameMillis;
	}

	public void setTargetFrameMillis(int targetFrameMillis) {
		this.targetFrameMillis = targetFrameMillis;
	}

	public int getTargetFrameTicks() {
		return targetFrameTicks;
	}

	public void setTargetFrameTicks(int targetFrameTicks) {
		this.targetFrameTicks = targetFrameTicks;
	}

	public Application(World world, String title, double zoom, int targetFrameMillis, int targetFrameTicks, boolean autoFrameTicks) {
		this.world = world;
		this.targetFrameMillis = targetFrameMillis;
		this.targetFrameTicks = targetFrameTicks;
		this.autoFrameTicks = autoFrameTicks;
		this.t = 0;
		
		this.frame = new JFrame(title);

		this.panel = new MainDisplayPanel(world, 0, 0, zoom);
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
		
		Substance[] toDisplay = {Substance.NRG, Substance.BODY, Substance.NUCLEIC, Substance.CHLOROPHYLL};;
		this.cellInfoPanel = new CellInfoPanel(toDisplay);
		frame.add(cellInfoPanel, BorderLayout.EAST);
        
        frame.pack();
        panel.zoomFit();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        this.lastFrame = System.currentTimeMillis();
        this.averageFrameTime = targetFrameMillis;
        
        this.loop = new Timer(targetFrameMillis, e -> {
        	// Start recording time for the clock
        	long startTime = System.currentTimeMillis();
        	
        	// Run the simulation
		    for (int i = 0; i<this.targetFrameTicks; i++) {
		    	world.tick();
		    }
		    
		    // Show selected cell, clear it if dead
		    if (panel.selectedCell != null && panel.selectedCell.isDead()) {
		    	panel.selectedCell = null; // TODO should this be handled here? Kind of weird that it has to be in such a weird place.
		    }
		    if (panel.selectedCell != null) {
			    cellInfoPanel.update(panel.selectedCell);
		    }
		    
		    // Paint frame
		    frame.repaint();
		    
		    // Measure frame time and update labels
		    long endTime = System.currentTimeMillis();
		    long frameTime = endTime - lastFrame;
		    long tickTime = endTime - startTime;
		    lastFrame = endTime;
		    System.out.println(frameTime + " " + tickTime);
		    averageFrameTime = averageFrameTime * (1-AVERAGE_DECAY_FACTOR) + frameTime * AVERAGE_DECAY_FACTOR;
		    fpsLabel.setText("fps: " + String.format("%.1f", 1000/averageFrameTime));
		    countLabel.setText("cells: " + world.getCells().size());
		    
		    // If autoset is on, determine new target tick speed
		    
		    
		});

		panel.setListener(this);
        
        setStatus("Ready to start.");
	}
	
	@Override
	public void setStatus(String s) {
		statusLabel.setText(s);
	}

	@Override
	public void cellClicked(Cell c) {
		cellInfoPanel.update(c);
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
}

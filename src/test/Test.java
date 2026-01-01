package test;

import java.awt.Dimension;

import javax.swing.JFrame;

import display.DisplayPanel;
import physics.TestBall;
import physics.LineWall;
import physics.Vector;
import physics.World;

public class Test {

	public static void main(String[] args) {
		// Test displaying a world
		World world = new World(100, 100);
		world.addEntity(new TestBall(world, new Vector(30, 50), new Vector(1, 0), 10, 10));
		world.addEntity(new TestBall(world, new Vector(50, 50), new Vector(0, 0), 10, 1));
//		world.addEntity(new TestBall(world, new Vector(50, 70), new Vector(0, -2), 10, 1));
		
		world.addWall(new LineWall(10, 10, 10, 90));
		world.addWall(new LineWall(10, 90, 90, 90));
		world.addWall(new LineWall(90, 90, 90, 10));
		world.addWall(new LineWall(90, 10, 10, 10));
		
		JFrame f = new JFrame("Test title");
		DisplayPanel d = new DisplayPanel(world, 0, 0, 4);
        f.add(d); //adds DisplayGraphics to the frame for viewing
        d.setPreferredSize(new Dimension(400,400));
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);     
        f.setVisible(true);
		
		while (true) {
//			System.out.println("Ball 1: " + ball1.getPos() + " " + ball1.getVel() + " Ball 2: " + ball2.getPos() + " " + ball2.getVel());
			d.repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			world.tick();
		}
		
//		// The copying speed test
//		Vector[] vectors = new Vector[1000];
//		long start = System.nanoTime();
//		for (int i=0; i<vectors.length; i++) {
//			vectors[i] = new Vector(0, 0);
//		}
//		double createTime = (System.nanoTime() - start) / 1e9;
//		Vector test = new Vector(Math.random(), Math.random());
//		start = System.nanoTime();
//		for (int i=0; i<vectors.length; i++) {
//			vectors[i].set(test);
//		}
//		double copyTime = (System.nanoTime() - start) / 1e9;
//		System.out.println(createTime + " v.s. " + copyTime);
	}

}

package test;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Map;

import javax.swing.JFrame;

import cell.Cell;
import display.DisplayPanel;
import genome.DNA;
import genome.Parser;
import genome.Program;
import physics.LineWall;
import physics.Vector;
import physics.World;

public class Test {

	public static void main(String[] args) {
		// Test parsing DNA into a cell
		String testString = "TAT TCT AAG AAT TAC";
		DNA testDNA = DNA.stringToDNA(testString);
		System.out.println("       DNA: " + testDNA);
		System.out.println("   Splices: " + Arrays.toString(testDNA.findSplices()));
		System.out.println("    Codons: " + Arrays.toString(testDNA.makeSplices(testDNA.findSplices())));
		System.out.println("W/o blanks: " + Arrays.toString(Parser.stripBlanks(testDNA.makeSplices(testDNA.findSplices()))));
		Program testProgram = Program.parseProgram(testDNA);
		System.out.println("   Program: " + Arrays.toString(testProgram.getStatements()));
		System.out.println("    Labels: ");
		Map<Integer, Integer> labels = testProgram.getLabels();
		for (int l : labels.keySet()) {
			System.out.println("       - " + String.format("%04X", l) + ": line " + labels.get(l));
		}
		
		World world = new World(100, 100);
		Cell cell = new Cell(world, new Vector(50, 50), new Vector(0, 0), 10, Math.PI / 2, testDNA, 0, 0, 0);
		world.addCell(cell);
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

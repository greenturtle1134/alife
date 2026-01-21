package test;

import java.util.Random;

import display.Application;
import physics.LineWall;
import physics.TestBall;
import physics.Vector;
import physics.World;

public class Test {

	public static void main(String[] args) {
		String testName = "Testing many balls";
		
//		String testString = "TAT TCT AAG AAT TAC";
//		DNA testDNA = DNA.stringToDNA(testString);
//		System.out.println("       DNA: " + testDNA);
//		System.out.println("   Splices: " + Arrays.toString(testDNA.findSplices()));
//		System.out.println("    Codons: " + Arrays.toString(testDNA.makeSplices(testDNA.findSplices())));
//		System.out.println("W/o blanks: " + Arrays.toString(Parser.stripBlanks(testDNA.makeSplices(testDNA.findSplices()))));
//		Program testProgram = Program.parseProgram(testDNA);
//		System.out.println("   Program: " + Arrays.toString(testProgram.getStatements()));
//		System.out.println("    Labels: ");
//		Map<Integer, Integer> labels = testProgram.getLabels();
//		for (int l : labels.keySet()) {
//			System.out.println("       - " + String.format("%04X", l) + ": line " + labels.get(l));
//		}
		
		World world = new World(100, 100);
		
//		CellInternals cellInternals = CellInternals.genDefault();
//		CellState cellState = new CellState(10, 100);
//		Cell cell = new Cell(world, new Vector(50, 50), new Vector(0, 0), Math.PI / 2, testDNA, 0, cellInternals, cellState);
//		world.addCell(cell);
		
		Random random = new Random(10);
		for (int i=0; i<10; i++) {
			world.addEntity(new TestBall(world, new Vector(random.nextDouble() * 50 + 25, random.nextDouble() * 50 + 25), new Vector(0, 0), 10, 1));
		}
		
//		world.addWall(new LineWall(10, 10, 10, 90));
//		world.addWall(new LineWall(10, 90, 90, 90));
//		world.addWall(new LineWall(90, 90, 90, 10));
//		world.addWall(new LineWall(90, 10, 10, 10));

		
		Application application = new Application(world, testName, 7.0, 20);
		application.run();
		
		
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

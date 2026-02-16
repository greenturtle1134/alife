package test;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import cell.Cell;
import cell.Substance;
import cell.WorldSettings;
import display.Application;
import genome.DNA;
import genome.Program;
import physics.TestBall;
import physics.Vector;
import physics.World;

public class Test {

	public static void main(String[] args) {		
		String testName = "alife test";
//		String testString = "TATAAACTAAATAGTAATGCAGAGAAATTAAGGCCAAATCCAAAGCCATTAAACATAAGAAAT";
		String testString = "TATAAAAGTTTAGAGTGATGGCCAAATCCAAAGAAGCCATTAAACATAAT";
		DNA testDNA = DNA.stringToDNA(testString);
		System.out.println("       DNA: " + testDNA);
		System.out.println("   Splices: " + Arrays.toString(testDNA.findSplices(DNA.TATA)));
		System.out.println("    Codons: " + Arrays.toString(testDNA.makeSplices(testDNA.findSplices(DNA.TATA))));
		Program testProgram = Program.parseProgram(testDNA);
		System.out.println("   Program: " + Arrays.toString(testProgram.getStatements()));
		System.out.println("    Labels: ");
		Map<Integer, Integer> labels = testProgram.getLabels();
		for (int l : labels.keySet()) {
			System.out.println("       - " + String.format("%04X", l) + ": line " + labels.get(l));
		}
		
		World world = new World(1000, 1000);
		
		Cell cell = new Cell(world, new Vector(100, 500), new Vector(0, 0), Math.PI / 2, testDNA, 50, 50);
		cell.addSubstance(Substance.CHLOROPHYLL.id, 50);
		world.addCell(cell);
//		world.selectedCell = cell;
		
//		Random random = new Random(10);
//		for (int i=0; i<10; i++) {
//			double r = random.nextDouble() * 10 + 5;
//			world.addEntity(new TestBall(world, new Vector(random.nextDouble() * 80 + 10, random.nextDouble() * 80 + 10), new Vector(0, 0), r, r*r));
//		}

		Application application = new Application(world, testName, 0.8, 10, 10);
//		application.runSaveVideo("..\\..\\Desktop\\alife screenshots\\frames");
		
		
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

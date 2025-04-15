package genome;

import java.util.Arrays;

import genome.Term.*;

public class GenomeTest {

	public static void main(String[] args) {
		/*
		 * Initial test with:
		 * 0 (AAA)  = START
		 * 1 (AAT)  = STOP
		 * 2 (AAG)  = INT (and take one codon)
		 * 3 (AAC)  = ADD
		 * 21 (TTT) = No Token
		 */
		String testString = "AAA TTT AAC AAG ATA TTT AAG ATC AAT";
		DNA testDNA = DNA.stringToDNA(testString);
		DNA.SplicePair[] testSplices = testDNA.findSplices();
		byte[] testSpliced = testDNA.makeSplices(testSplices);
		
		System.out.println(testDNA);
		System.out.println(Arrays.toString(testSplices));
		System.out.println(Arrays.toString(testSpliced));
		
//		Parser testParser = new Parser(testSpliced);
//		testParser.nextCodon();
//		Operator testOperator = testParser.nextOperator(0);
//		System.out.println(testOperator);
//		CellContext c = new CellContext();
//		System.out.println(testOperator.eval(c));
	}

}

package genome;

import java.util.Arrays;
import java.util.TreeMap;

public class GenomeTest {

	public static void main(String[] args) {
		/*
		 * Initial test with:
		 * 0 (AAA)  = BLANK / ESCAPE
		 * 1 (AAT)  = STOP
		 * 2 (AAG)  = INT (and take one codon)
		 * 3 (AAC)  = ADD
		 * 4 (ATA)  = PRINT (statement)
		 * 5 (ATT)  = ANS
		 * 16 (TAA) = Label
		 * 17 (TAT) = START
		 * 18 (TAG) = LABELJUMP
		 * 19 (TAC) = LT
		 * 21 (TTT) = BLANK
		 * 42 (GGG) = BLANK
		 * 63 (CCC) = BLANK
		 * All non-defined treated as NOPs
		 */
		String testString = "TAT TAA AAA GGG AAA GGG AAC ATT AAG AAG ATA ATT TAG AAA GGG AAA GGG TAC ATT AAG AGA AAT";
		DNA testDNA = DNA.stringToDNA(testString);
		System.out.println("       DNA: " + testDNA);
		System.out.println("   Splices: " + Arrays.toString(testDNA.findSplices()));
		System.out.println("    Codons: " + Arrays.toString(testDNA.makeSplices(testDNA.findSplices())));
		System.out.println("W/o blanks: " + Arrays.toString(Parser.stripBlanks(testDNA.makeSplices(testDNA.findSplices()))));
		Program testProgram = Program.parseProgram(testDNA);
		System.out.println("   Program: " + Arrays.toString(testProgram.getStatements()));
		System.out.println("    Labels: ");
		TreeMap<Integer, Integer> labels = testProgram.getLabels();
		for (int l : labels.keySet()) {
			System.out.println("       - " + String.format("%04X", l) + ": line " + labels.get(l));
		}
		
		System.out.println("\nExecuting...");
		TestCell c = new TestCell();
		testProgram.run(c);
		
//		char[] testChars = {'E', 'J', 'K', 'M'};
//		System.out.println(testDNA.toString(testChars));
	}

}

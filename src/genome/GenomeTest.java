package genome;

import java.util.Arrays;

public class GenomeTest {

	public static void main(String[] args) {
		/*
		 * Initial test with:
		 * 0 (AAA)  = BLANK
		 * 1 (AAT)  = STOP
		 * 2 (AAG)  = INT (and take one codon)
		 * 3 (AAC)  = ADD
		 * 4 (ATA)  = PRINT (statement)
		 * 5 (ATT)  = ANS
		 * 16 (TAA) = Label
		 * 17 (TAT) = START
		 * 21 (TTT) = No Token
		 * All non-defined treated as NOPs
		 */
		String testString = "TAT TTT AAC AAG ATA GGG TTT AAG TAC TTT TAA GAA TTT CCC ATA CCC TTT AAC ATT GGG ATT TAA AAT";
		DNA testDNA = DNA.stringToDNA(testString);
		System.out.println("       DNA: " + testDNA);
		System.out.println("   Splices: " + Arrays.toString(testDNA.findSplices()));
		System.out.println("    Codons: " + Arrays.toString(testDNA.makeSplices(testDNA.findSplices())));
		System.out.println("W/o blanks: " +Arrays.toString(Parser.stripBlanks(testDNA.makeSplices(testDNA.findSplices()))));
		Program testProgram = Program.parseProgram(testDNA);
		System.out.println("   Program: " +Arrays.toString(testProgram.getStatements()));
		
		System.out.println("\nExecuting...");
		Cell c = new Cell();
		ExecContext e = new ExecContext();
		testProgram.run(c, e);
		
//		char[] testChars = {'E', 'J', 'K', 'M'};
//		System.out.println(testDNA.toString(testChars));
	}

}

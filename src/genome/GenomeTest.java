package genome;

import java.util.Arrays;
import java.util.Map;

public class GenomeTest {

	public static void main(String[] args) {
		/*
		 * Initial test with:
		 * 0 (AAA)  = BLANK / ESCAPE
		 * 1 (AAT)  = LOAD(<source>)
		 * 2 (AAG)  = INT(<value>)
		 * 3 (AAC)  = STORE(<dest>, value)
		 * 4 (ATA)  = PRINT(value)
		 * 5 (ATT)  = ANS()
		 * 16 (TAA) = Label(<name1>, <name2>)
		 * 17 (TAT) = START
		 * 18 (TAG) = LABELJUMP(<name1>, <name2>)
		 * 19 (TAC) = STOP
		 * 21 (TTT) = BLANK
		 * 24 (TGA) = ADD(a, b)
		 * 25 (TGT) = SUB(a, b)
		 * 26 (TGG) = MULT(a, b)
		 * 27 (TGC) = DIV(a, b)
		 * 28 (TCA) = LT(a, b)
		 * 29 (TCT) = MOVEF(a)
		 * 30 (TCG) = MOVER(a)
		 * 42 (GGG) = BLANK
		 * 63 (CCC) = BLANK
		 * All non-defined treated as NOPs
		 */
//		String testString = "TAT AAG AAT TAA AAG GAA AAC AAT TGA AAG AAT AAT AAT TGG ATT AAT AAT TAG AAG GAA TCA AAT AAT AAG ATA ATA ATT TAC";
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
		
//		System.out.println("\nExecuting...");
//		Cell c = new Cell();
//		testProgram.run(c);
		
//		char[] testChars = {'E', 'J', 'K', 'M'};
//		System.out.println(testDNA.toString(testChars));
	}

}

package genome;

import java.util.Arrays;

public class GenomeTest {

	public static void main(String[] args) {
		/*
		 * Initial test with:
		 * 0 (AAA)  = START
		 * 1 (AAT)  = STOP
		 * 2 (AAG)  = INT (and take one codon)
		 * 3 (AAC)  = ADD
		 * 4 (ATA)  = PRINT (statement)
		 * 5 (ATT)  = ANS
		 * 16 (TAA) = Label
		 * 21 (TTT) = No Token
		 * All non-defined treated as NOPs
		 */
		String testString = "AAA TTT AAC AAG ATA TTT AAG TAC TTT TAA GAA TTT ATA TTT AAC ATT ATT AAT";
		DNA testDNA = DNA.stringToDNA(testString);
		Program testProgram = Program.parseProgram(testDNA);
		Cell c = new Cell();
		ExecContext e = new ExecContext();
		
		System.out.println(Arrays.toString(testProgram.getStatements()));
		System.out.println("Executing...");
		testProgram.run(c, e);
		
//		char[] testChars = {'E', 'J', 'K', 'M'};
//		System.out.println(testDNA.toString(testChars));
	}

}

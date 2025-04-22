package genome;

import java.util.ArrayList;
import java.util.Arrays;

import genome.Term.Statement;

public class GenomeTest {

	public static void main(String[] args) {
		/*
		 * Initial test with:
		 * 0 (AAA)  = START
		 * 1 (AAT)  = STOP
		 * 2 (AAG)  = INT (and take one codon)
		 * 3 (AAC)  = ADD
		 * 4 (ATA)  = PRINT (statement)
		 * 21 (TTT) = No Token
		 */
		String testString = "AAA TTT ATA TTT AAC AAG ATA TTT AAG ATC ATA TTT AAG TAA TTT AAT";
		DNA testDNA = DNA.stringToDNA(testString);
		DNA.SplicePair[] testSplices = testDNA.findSplices();
		byte[] testSpliced = testDNA.makeSplices(testSplices);
		
		System.out.println(testDNA);
		System.out.println(Arrays.toString(testSplices));
		System.out.println(Arrays.toString(testSpliced));
		
		Parser testParser = new Parser(testSpliced);
		Statement current = testParser.nextStatement();
		ArrayList<Statement> statements = new ArrayList<Statement>();
		while (current != null) {
			statements.add(current);
			System.out.println(current);
			current = testParser.nextStatement();
		}
		CellContext c = new CellContext();
		for (Statement s : statements) {
			s.exec(c);
		}
	}

}

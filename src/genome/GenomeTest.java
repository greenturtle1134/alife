package genome;

import java.util.Arrays;
import java.util.Map;

public class GenomeTest {

	public static void main(String[] args) {
//		String testString = "TAT AAG AAT TAA AAG GAA AAC AAT TGA AAG AAT AAT AAT TGG ATT AAT AAT TAG AAG GAA TCA AAT AAT AAG ATA ATA ATT TAC";
		String testString = "TATAAA GTA TAC TAT CAA GTT TTA CCC TAC";
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
	}

}

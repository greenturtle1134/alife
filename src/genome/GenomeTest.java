package genome;

import java.util.Arrays;

public class GenomeTest {

	public static void main(String[] args) {
		String testString = "AAATCGAAAGCTTAAAGTCC";
		DNA testDNA = DNA.stringToDNA(testString);
		DNA.SplicePair[] testSplices = testDNA.findSplices();
		byte[] testSpliced = testDNA.makeSplices(testSplices);
		
		System.out.println(testDNA);
		System.out.println(Arrays.toString(testSplices));
		System.out.println(Arrays.toString(testSpliced));
	}

}

package genome;

import genome.Term.*;

public class Parser {
	
	private byte[] codons;
	private int i;
	
	/*
	 * Initial test with:
	 * 0 (AAA) = START
	 * 1 (TAA) = STOP
	 * 2 (GAA) = INT (and take one codon)
	 * 3 (CAA) = ADD
	 */
	
//	public static boolean isRelevant(byte codon) {
//		return (codon != 0 && codon != 1);
//	}
//	
//	public static boolean isOperator(byte codon) {
//		return codon == 2 || codon == 3;
//	}
	
	public Parser(byte[] codons) {
		this.codons = codons;
		this.i = 0;
	}
	
	public byte nextCodon() {
		return codons[i++];
	}
	
	public Operator nextOperator(int defaultVal) {
		switch (codons[i++]) {
		case 2:
			return new Term.Int(nextCodon());
		case 3:
			return new Term.Add(nextOperator(0), nextOperator(0));
		default:
			i--;
			return new Term.Int(defaultVal);
		}
	}
	
}

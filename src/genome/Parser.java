package genome;

import genome.Term.*;

public class Parser {
	
	private byte[] codons;
	private int i;
	
	/**
	 * Looks up if a codon is irrelevant (should be skipped as if it doesn't exist)
	 * @param codon - the codon to be queried
	 */
	public static boolean isIrrelevant(byte codon) {
		return codon == 0 || codon == 1 || codon == 21;
	}
	
	public Parser(byte[] codons) {
		int c = 0;
		for (int i=0; i<codons.length; i++) {
			if (!isIrrelevant(codons[i])) {
				c++;
			}
		}
		this.codons = new byte[c];
		int j = 0;
		for (int i=0; i<codons.length; i++) {
			if (!isIrrelevant(codons[i])) {
				this.codons[j++] = codons[i];
			}
		}
		
		this.i = 0;
	}
	
	/**
	 * Checks if the parser is currently at the end of the message
	 */
	public boolean isEnd() {
		return i >= codons.length;
	}
	
	/**
	 * Returns the current codon and advances the parser to the next relevant codon
	 */
	public byte nextCodon() {
		return codons[i++];
	}
	
	public Operator nextOperator() {
		if (isEnd()) {
			return null;
		}
		byte c = nextCodon();
		switch (c) {
		case 2:
			return new Term.Int(nextCodon());
		case 3:
			return new Term.Add(nextOperator(0), nextOperator(0));
		default:
			i--;
			return null;
		}
	}
	
	public Operator nextOperator(int defaultVal) {
		Operator res = nextOperator();
		if (res != null) {
			return res;
		}
		else {
			return new Term.Int(defaultVal);
		}
	}
	
	public Statement nextStatement() {
		if (isEnd()) {
			return null;
		}
		Operator res = nextOperator();
		if (res != null) {
			// TODO Handle case where we get an operator instead of statement!
			return null;
		}
		byte c = nextCodon();
		switch (c) {
			case 4:
				return new Term.Print(nextOperator(0));
			default:
				i--;
				return null;
		}
	}
	
}

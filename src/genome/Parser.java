package genome;

import genome.Term.*;

public class Parser {
	
	private byte[] codons;
	private int i;
	
	public static boolean isIrrelevant(byte codon) {
		return codon == 21;
	}
	
	public Parser(byte[] codons) {
		this.codons = codons;
		this.i = 0;
	}
	
	public boolean isEnd() {
		return i >= codons.length;
	}
	
	public byte nextCodon() {
		byte res = codons[i++];
		while (!isEnd() && isIrrelevant(codons[i])) {
			i++;
		}
		return res;
	}
	
	public Operator nextOperator() {
		if (isEnd()) {
			return null;
		}
		switch (nextCodon()) {
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
	
}

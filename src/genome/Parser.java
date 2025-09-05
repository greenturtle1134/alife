package genome;

import java.util.Arrays;

import genome.Term.*;

/**
 * Object representing a codon sequence in the process of parsing.
 * 
 * This file contains the logic for codon interpretation.
 */
public class Parser {
	
	private static final byte BYTE_ZERO = (byte) 0;
	private static final byte BYTE_NEG1 = (byte) -1;
	public static final int C_ESCAPE = 0;
	public static final int C_BLANK1 = 21;
	public static final int C_BLANK2 = 42;
	public static final int C_BLANK3 = 63;
	
	private byte[] codons;
	private int i;
	
	/**
	 * Removes all "blank" codons (taking blank-escape into account) from a codon list.
	 * This is a step that is applied pre-parsing.
	 * 
	 * @param codons - the codon list to process
	 * @return Same codon list without blanks
	 */
	public static byte[] stripBlanks (byte[] codons) {
		byte[] res = new byte[codons.length];
		int c = 0;
		boolean escape = false;
		for (int i=0; i<codons.length; i++) {
			if (escape) {
				res[c++] = codons[i];
				escape = false;
			}
			if (codons[i] == C_ESCAPE) {
				escape = true;
			}
			else if (!(codons[i] == C_BLANK1 || codons[i] == C_BLANK2 || codons[i] == C_BLANK3 || codons[i] == DNA.C_START || codons[i] == DNA.C_END)) {
				res[c++] = codons[i];
			}
		}
		return Arrays.copyOfRange(res, 0, c);
	}
	
	public Parser(byte[] codons) {
		this.codons = codons;
		this.i = 0;
	}
	
	/**
	 * Checks if the parser is currently at the end of the codon sequence
	 */
	public boolean isEnd() {
		return i >= codons.length;
	}
	
	/**
	 * Gets the next codon and advances the pointer. If the end is reached, returns defaultVal and leaves the pointer unchanged.
	 * @param defaultVal - default value if end is reached
	 * @return the next codon
	 */
	public byte nextCodon(byte defaultVal) {
		if (isEnd()) {
			return defaultVal;
		}
		else {
			return codons[i++];
		}
	}
	
	/**
	 * Gets the next codon and advances the pointer. If the end is reached, returns -1 and leaves the pointer unchanged.
	 * Wraps nextCodon(defaultVal) with a default of -1.
	 * @return the next codon
	 */
	public byte nextCodon() {
		return this.nextCodon(BYTE_NEG1);
	}
	
	/**
	 * Parses the next operator.
	 * If the end of the sequence is reached, returns null.
	 * If the next symbol cannot be recognized as an operator, returns null and does not advance pointer.
	 * @return the next operator
	 */
	public Operator nextOperator() {
		byte c = nextCodon();
		switch (c) {
		case -1: // End of sequence reached
			return null;
		case 1:
			return new Term.Read(nextCodon());
		case 2:
			return new Term.Int(nextCodon());
		case 5: 
			return new Term.Read(0);
		case 24:
			return new Term.Add(nextOperator(0), nextOperator(0));
		case 25:
			return new Term.Sub(nextOperator(0), nextOperator(0));
		case 26:
			return new Term.Mult(nextOperator(0), nextOperator(0));
		case 27:
			return new Term.Div(nextOperator(0), nextOperator(0));
		case 28:
			return new Term.LT(nextOperator(0), nextOperator(0));
		default: // Codon not recognized as operator
			i--;
			return null;
		}
	}
	
	/**
	 * As nextOperator(), but with a default value.
	 * @param defaultVal - integer
	 * @return the next operator, or Int(defaultVal) if none exists
	 */
	public Operator nextOperator(int defaultVal) {
		Operator res = nextOperator();
		if (res != null) {
			return res;
		}
		else {
			return new Term.Int(defaultVal);
		}
	}
	
	/**
	 * Parse the next statement.
	 * If the end of the sequence is reached, returns null.
	 * If the next codon cannot be interpreted as a statement (or operator), parses it as a NOP and advances past it.
	 * @return the next statement
	 */
	public Statement nextStatement() {
		Operator res = nextOperator();
		if (res != null) {
			return new Term.Store(0, res);
		}
		byte c = nextCodon();
		switch (c) {
			case -1: // End of sequence reached
				return null;
			case 3:
				return new Term.Store(nextCodon(BYTE_ZERO), nextOperator(0));
			case 4:
				return new Term.Print(nextOperator(0));
			case 16:
				return new Term.Label(nextCodon(BYTE_ZERO) * 64 + nextCodon(BYTE_ZERO));
			case 18:
				return new Term.JumpLabel(nextCodon(BYTE_ZERO) * 64 + nextCodon(BYTE_ZERO), nextOperator(1));
			default:
				return new Term.Nop();
		}
	}
	
}

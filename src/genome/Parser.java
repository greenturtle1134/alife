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
	
	private static final byte ENDOP_CODON = (byte) 1;
	private static final byte ENDLINE_CODON = (byte) 2;
	
	private byte[] codons;
	private int i;
	
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
	 * Gets the next codon without pointer advancement, returning -1 if there is none
	 * @return the next codon
	 */
	public byte peekCodon() {
		if (isEnd()) {
			return BYTE_NEG1;
		}
		else {
			return codons[i];
		}
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
	 * Parses the next operator.
	 * If the end of the sequence is reached, returns null.
	 * If the next symbol cannot be recognized as an operator, returns null and does not advance pointer.
	 * @return the next operator
	 */
	public Operator nextOperator() {
		byte c = nextCodon();
		Operator res;
		switch (c) {
		case -1: // End of sequence reached
			return null; // Backtrack not needed as already at end
		case 16:
			res = new Term.Int(0);
			break;
		case 17:
			res = new Term.Int(1);
			break;
		case 18:
			res = new Term.Int(2);
			break;
		case 19:
			res = new Term.ReadCodon(0); // TODO replace with wherever ANS is
			break;
		case 20:
			res = new Term.Int(nextCodon(BYTE_ZERO));
			break;
		// if int variants exist they may go here
		case 24:
			res = new Term.Neg(nextOperator(0));
			break;
		case 25:
			res = new Term.Inc(nextOperator(0));
			break;
		case 26:
			res = new Term.Not(nextOperator(0));
			break;
		// if I include Pow2 it may go here
		case 28:
			res = new Term.Double(nextOperator(0));
			break;
		case 29:
			res = new Term.Half(nextOperator(0));
			break;
		// if I include Times64 and Div64 they may go here
		case 32:
			res = new Term.ReadCodon(nextCodon(BYTE_ZERO));
			break;
		case 33:
			res = new Term.ReadInt(nextOperator(0));
			break;
		case 34:
			res = new Term.Amount(nextOperator(0));
			break;
		case 35:
			res = new Term.Sense(nextOperator(0));
			break;
		case 36:
			res = new Term.Add(nextOperator(0), nextOperator(0));
			break;
		case 37:
			res = new Term.Sub(nextOperator(0), nextOperator(0));
			break;
		case 38:
			res = new Term.Mult(nextOperator(0), nextOperator(1));
			break;
		case 39:
			res = new Term.Div(nextOperator(0), nextOperator(1));
			break;
		case 40:
			res = new Term.And(nextOperator(0), nextOperator(1)); // do I want this to default to 1?
			break;
		case 41:
			res = new Term.Or(nextOperator(0), nextOperator(0));
			break;
		case 42:
			res = new Term.Eq(nextOperator(0), nextOperator(0));
			break;
		case 43:
			res = new Term.Neq(nextOperator(0), nextOperator(0));
			break;
		case 44:
			res = new Term.LT(nextOperator(0), nextOperator(0));
			break;
		case 45: {
			Operator a = nextOperator(0);
			Operator b = nextOperator(0);
			res = new Term.LT(b, a);
		}
		break;
		case 46:
			res = new Term.LEQ(nextOperator(0), nextOperator(0));
			break;
		case 47: {
			Operator a = nextOperator(0);
			Operator b = nextOperator(0);
			res = new Term.LEQ(b, a);
		}
		break;
//		case ENDLINE_CODON: // EndLine means all operators must close
		default: // Codon not recognized as operator
			// In both these cases, back off and return null
			i--;
			return null;
		}
		
//		// We have closed an operator, so move past one EndOp
//		if (peekCodon() == ENDOP_CODON) {
//			nextCodon();
//		}
		
		return res;
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
			return new Term.StoreCodon(0, res); // Term is an operator = store into ANS
		}
		byte c = nextCodon();
//		while (c == ENDLINE_CODON || c == ENDOP_CODON) { // Since a new statement is starting, we are permitted to move beyond EndLine, or EndOp for that matter
//			c = nextCodon();
//		}
		switch (c) {
			case -1: // End of sequence reached
				return null;
			case 4:
				return new Term.Action(0);
			case 5:
				return new Term.Action(1);
			case 6:
				return new Term.Action(2);
			case 7:
				return new Term.Action(nextCodon(BYTE_ZERO));
			case 8:
				return new Term.Label(nextCodon(BYTE_ZERO));
			case 9:
				return new Term.JumpLabel(nextCodon(BYTE_ZERO), nextOperator(1));
			// if jump variants exist, they might go here
			case 12:
				return new Term.Skip(nextOperator(1));
			case 13:
				return new Term.Backskip(nextOperator(1));
			case 14:
				return new Term.Wait(nextOperator(1));
			case 15:
				return new Term.Unskip();
			case 48:
				return new Term.Move(nextOperator(0), false, false);
			case 49:
				return new Term.Move(nextOperator(0), false, true);
			case 50:
				return new Term.Move(nextOperator(0), true, false);
			case 51:
				return new Term.Move(nextOperator(0), true, true);
			case 52:
				return new Term.Turn(nextOperator(0), false, false);
			case 53:
				return new Term.Turn(nextOperator(0), false, true);
			case 54:
				return new Term.Turn(nextOperator(0), true, false);
			case 55:
				return new Term.Turn(nextOperator(0), true, true);
			case 56:
				return new Term.StoreCodon(nextCodon(BYTE_ZERO), nextOperator(0));
			case 57:
				return new Term.StoreInt(nextOperator(0), nextOperator(0));
			// AddTo might go here
			case 60:
				return new Term.Build(nextOperator(0), nextOperator(1));
			case 61:
				return new Term.Burn(nextOperator(0), nextOperator(1));
			case 62:
				return new Term.Expel(nextOperator(0), nextOperator(0));
			case 63:
				return new Term.Attack(nextOperator(0), nextOperator(0));
			default:
				return new Term.Nop();
		}
	}
	
}

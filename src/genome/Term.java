package genome;

import cell.Cell;
import cell.Substance;

import static utils.Utils.fractionRound; 

public abstract class Term {
	
	public static String regName(int x) {
		if (x == 64) {
			return "ANS";
		}
		else {
			return x+"";
		}
	}
	
	public static abstract class Statement extends Term {
		public abstract void exec(Cell c);
	}

	public static abstract class Operator extends Term {
		public abstract int eval(Cell c);
	}
	
	public static class Nop extends Statement{
		public void exec(Cell c) {
			// Do nothing
		}
		
		public String toString() {
			return "Nop()";
		}
	}

	public static String actionName(int x) {
		switch (x) {
		case 0:
			return "Repro";
		case 1:
			return "AllStop";
		case 2:
			return "BreakTies";
		default:
			return x+"";
		}
	}
	
	public static class Action extends Statement{
		private int action;
		
		public Action(int action) {
			this.action = action;
		}
		
		public void exec(Cell c) {
			switch (action) {
			case 0: // Repro
				c.repro();
				break;
			case 1: // AllStop
				c.moveF = 0;
				c.moveR = 0;
				break;
			case 2: // BreakTies
				// Nothing for now because I haven't implemented ties
				break;
			}
		}
		
		public int getAction() {
			return action;
		}
		
		public String toString() {
			return "Action<" + actionName(action) + ">";
		}
	}
	
	public static class Label extends Statement {
		private int label;
		
		public Label(int label) {
			this.label = label;
		}
		
		public void exec(Cell c) {
			// Do nothing
		}
		
		public int getLabel() {
			return label;
		}
		
		public String toString() {
			return "Label<"+label+">";
		}
	}
	
	public static class JumpLabel extends Statement {
		private int target;
		private Operator cond;
		
		public JumpLabel(int target, Operator cond) {
			this.target = target;
			this.cond = cond;
		}
		
		public void exec(Cell c) {
			if (cond.eval(c) != 0) {
				c.labelJump(target);
			}
		}
		
		public String toString() {
			return "JumpLabel<"+target+">(" + cond + ")";
		}
	}
	
	// TODO *exactly* what jump options do I want to permit? Forward, backward, by codon, relative, absolute?
	
//	public static class JumpDelta extends Statement {
//		private Operator a;
//		private Operator cond;
//		
//		public JumpDelta(Operator a, Operator cond) {
//			this.a = a;
//			this.cond = cond;
//		}
//		
//		public void exec(Cell c) {
//			
//		}
//	}
	
	public static class Skip extends Statement {
		private Operator cond;
		
		public Skip(Operator cond) {
			this.cond = cond;
		}
		
		public void exec(Cell c) {
			if (cond.eval(c) != 0) {
				// TODO implement skip
			}
		}
		
		public String toString() {
			return "Skip(" + cond + ")";
		}
	}
	
	public static class Backskip extends Statement {
		private Operator cond;
		
		public Backskip(Operator cond) {
			this.cond = cond;
		}
		
		public void exec(Cell c) {
			if (cond.eval(c) != 0) {
				// TODO implement skip
			}
		}
		
		public String toString() {
			return "Backskip(" + cond + ")";
		}
	}
	
	public static class Wait extends Statement {
		private Operator a;
		
		public Wait(Operator a) {
			this.a = a;
		}
		
		public void exec(Cell c) {
			int time = a.eval(c);
			c.setWait(time);
		}
		
		public String toString() {
			return "Wait(" + a + ")";
		}
	}
	
	public static class Unskip extends Statement{
		public void exec(Cell c) {
			// Do nothing
		}
		
		public String toString() {
			return "Nop()";
		}
	}
	
	// TODO decide if I want a "restart" statement?
	
	public static class Int extends Operator {
		private int val;
		
		public Int(int val) {
			this.val = val;
		}
		
		public int eval(Cell c) {
			return val;
		}
		
		public String toString() {
			return ""+val;
		}
	}
	
	public static class Neg extends Operator {
		private Operator a;
		
		public Neg(Operator a) {
			this.a = a;
		}
		
		public int eval(Cell c) {
			return -a.eval(c);
		}
		
		public String toString() {
			return "Neg(" + a + ")";
		}
	}
	
	public static class Inc extends Operator {
		private Operator a;
		
		public Inc(Operator a) {
			this.a = a;
		}
		
		public int eval(Cell c) {
			return a.eval(c) + 1;
		}
		
		public String toString() {
			return "Inc(" + a + ")";
		}
	}
	
	public static class Not extends Operator {
		private Operator a;
		
		public Not(Operator a) {
			this.a = a;
		}
		
		public int eval(Cell c) {
			return (a.eval(c) == 0) ? 1 : 0;
		}
		
		public String toString() {
			return "Not(" + a + ")";
		}
	}
	
	// TODO: Implement integer Pow2 if I want it
	
	public static class Double extends Operator {
		private Operator a;
		
		public Double(Operator a) {
			this.a = a;
		}
		
		public int eval(Cell c) {
			return a.eval(c) * 2;
		}
		
		public String toString() {
			return "Double(" + a + ")";
		}
	}
	
	public static class Half extends Operator {
		private Operator a;
		
		public Half(Operator a) {
			this.a = a;
		}
		
		public int eval(Cell c) {
			return a.eval(c) / 2;
		}
		
		public String toString() {
			return "Half(" + a + ")";
		}
	}
	
	// TODO implement Times64 and Divide64 if I deem those necessary
	
	public static class ReadCodon extends Operator {
		private int a;
		
		public ReadCodon(int a) {
			this.a = a;
		}
		
		public int eval(Cell c) {
			return c.memGet(a);
		}
		
		public String toString() {
			return "$" + regName(a);
		}
	}
	
	public static class ReadInt extends Operator {
		private Operator a;
		
		public ReadInt(Operator a) {
			this.a = a;
		}
		
		public int eval(Cell c) {
			return c.memGet(a.eval(c));
		}
		
		public String toString() {
			return "$(" + a + ")";
		}
	}
	
	public static class Amount extends Operator {
		private Operator a;
		
		public Amount(Operator a) {
			this.a = a;
		}
		
		public int eval(Cell c) {
			int a0 = a.eval(c);
			if (a0 >= 0) { // Positive value: give amount of substance
				int s = Substance.numToID(a0);
				if (s != -1) {
					return (int) Math.floor(c.substances[s]);
				}
				else {
					return 0;
				}
			}
			else { // Negative value: give quantity filled
				a0 = -a0;
				if (a0 == 1) {
					// Special case: if BODY quantity is requested, get NRG quantity instead (as -0 is not a thing)
					// This does kind of hardcode that 0=Nrg and 1=Body but ehh
					a0 = 0;
				}
				int s = Substance.numToID(a0);
				if (s == -1) {
					// No substance corresponding to this ID
					return 0;
				}
				else if (s == Substance.NUCLEIC.id) {
					// Special case: return fraction of genome size, not actual capacity
					return fractionRound(c.substances[s] / c.getDna().getLength(), 64);
				}
				else {
					return fractionRound(c.substances[s] / c.getCapacity(s), 64);
				}
			}
		}
		
		public String toString() {
			return "Amount(" + a + ")";
		}
	}
	
	public static class Sense extends Operator {
		private Operator a;
		
		public Sense(Operator a) {
			this.a = a;
		}
		
		public int eval(Cell c) {
			// TODO: Implement Sense
			return 0;
		}
		
		public String toString() {
			return "Sense(" + a + ")";
		}
	}
	
	public static class Add extends Operator {
		private Operator a, b;
		
		public Add(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(Cell c) {
			return a.eval(c) + b.eval(c);
		}
		
		public String toString() {
			return "Add(" + a + "," + b + ")";
		}
	}
	
	public static class Sub extends Operator {
		private Operator a, b;
		
		public Sub(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(Cell c) {
			return a.eval(c) - b.eval(c);
		}
		
		public String toString() {
			return "Sub(" + a + "," + b + ")";
		}
	}
	
	public static class Mult extends Operator {
		private Operator a, b;
		
		public Mult(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(Cell c) {
			return a.eval(c) * b.eval(c);
		}
		
		public String toString() {
			return "Mult(" + a + "," + b + ")";
		}
	}
	
	public static class Div extends Operator {
		private Operator a, b;
		
		public Div(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(Cell c) {
			int b0 = b.eval(c);
			if (b0 != 0) {
				return a.eval(c) / b.eval(c);
			}
			else {
				return a.eval(c);
			}
		}
		
		public String toString() {
			return "Div(" + a + "," + b + ")";
		}
	}
	
	public static class And extends Operator {
		private Operator a, b;
		
		public And(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(Cell c) {
			if ((a.eval(c) != 0) && (b.eval(c) != 0)) {
				return 1;
			}
			else {
				return 0;
			}
		}
		
		public String toString() {
			return "And(" + a + "," + b + ")";
		}
	}
	
	public static class Or extends Operator {
		private Operator a, b;
		
		public Or(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(Cell c) {
			if ((a.eval(c) != 0) || (b.eval(c) != 0)) {
				return 1;
			}
			else {
				return 0;
			}
		}
		
		public String toString() {
			return "Or(" + a + "," + b + ")";
		}
	}
	
	public static class Eq extends Operator {
		private Operator a, b;
		
		public Eq(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(Cell c) {
			if (a.eval(c) == b.eval(c)) {
				return 1;
			}
			else {
				return 0;
			}
		}
		
		public String toString() {
			return "EQ(" + a + "," + b + ")";
		}
	}
	
	public static class Neq extends Operator {
		private Operator a, b;
		
		public Neq(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(Cell c) {
			if (a.eval(c) == b.eval(c)) {
				return 0;
			}
			else {
				return 1;
			}
		}
		
		public String toString() {
			return "NEQ(" + a + "," + b + ")";
		}
	}
	
	public static class LT extends Operator {
		private Operator a, b;
		
		public LT(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(Cell c) {
			if (a.eval(c) < b.eval(c)) {
				return 1;
			}
			else {
				return 0;
			}
		}
		
		public String toString() {
			return "LT(" + a + "," + b + ")";
		}
	}
	
	public static class LEQ extends Operator {
		private Operator a, b;
		
		public LEQ(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(Cell c) {
			if (a.eval(c) <= b.eval(c)) {
				return 1;
			}
			else {
				return 0;
			}
		}
		
		public String toString() {
			return "LEQ(" + a + "," + b + ")";
		}
	}
	
	// I implemented Move in this odd way. Is this truly best?
	public static class Move extends Statement {
		private Operator a;
		private boolean isRL;
		private boolean isNeg;
		
		public static final String[] DIRECTION_STRINGS = {"F", "B", "R", "L"};
		
		public Move(Operator a, boolean isRL, boolean isNeg) {
			this.a = a;
			this.isRL = isRL;
			this.isNeg = isNeg;
		}
		
		public void exec(Cell c) {
			int amt = this.a.eval(c);
			if (isNeg) {
				amt = -amt;
			}
			if (isRL) {
				c.moveR = amt;
			}
			else {
				c.moveF = amt;
			}
		}
		
		public String toString() {
			return "Move"+DIRECTION_STRINGS[(isRL ? 2 : 0) + (isNeg ? 1 : 0)] + "(" + a + ")";	
		}
	}
	
	public static class Turn extends Statement {
		private Operator a;
		private boolean isFine;
		private boolean isLeft;
		
		public Turn(Operator a, boolean isFine, boolean isLeft) {
			this.a = a;
			this.isFine = isFine;
			this.isLeft = isLeft;
		}
		
		public void exec(Cell c) {
			c.rotRequest += (isLeft ? -1 : 1) * a.eval(c) / (isFine ? 256.0 : 64.0); // TODO maybe a parameter for fine turning ratio?
		}
		
		public String toString() {
			return (isFine ? "TurnFine" : "Turn") + (isLeft ? "L" : "R") + "(" + a + ")";
		}
	}
	
	public static class StoreCodon extends Statement {
		private Operator a;
		private int i;
		
		public StoreCodon(int i, Operator a) {
			this.a = a;
			this.i = i;
		}
		
		public void exec(Cell c) {
			c.memSet(i, a.eval(c));
		}
		
		public String toString() {
			return "$"+regName(i) + " <- " + a;
		}
	}
	
	public static class StoreInt extends Statement {
		private Operator a;
		private Operator index;
		
		public StoreInt(Operator index, Operator a) {
			this.a = a;
			this.index = index;
		}
		
		public void exec(Cell c) {
			c.memSet(index.eval(c), a.eval(c));
		}
		
		public String toString() {
			return "$("+ a + ") <- " + a;
		}
	}
	
	// TODO: Implement the AddTo if I want them
	
	public static class Build extends Statement {
		private Operator target;
		private Operator amount;
		
		public Build(Operator target, Operator amount) {
			this.target = target;
			this.amount = amount;
		}
		
		public void exec(Cell c) {
			c.build(Substance.numToID(target.eval(c)), amount.eval(c));
		}
		
		public String toString() {
			return "Build(" + target + "," + amount + ")";
		}
	}
	
	public static class Burn extends Statement {
		private Operator target;
		private Operator amount;
		
		public Burn(Operator target, Operator amount) {
			this.target = target;
			this.amount = amount;
		}
		
		public void exec(Cell c) {
			c.burn(Substance.numToID(target.eval(c)), amount.eval(c));
		}
		
		public String toString() {
			return "Burn(" + target + "," + amount + ")";
		}
	}
	
	public static class Expel extends Statement {
		private Operator target;
		private Operator amount;
		
		public Expel(Operator target, Operator amount) {
			this.target = target;
			this.amount = amount;
		}
		
		public void exec(Cell c) {
			// TODO implement Expel
		}
		
		public String toString() {
			return "Expel(" + target + "," + amount + ")";
		}
	}
	
	public static class Attack extends Statement {
		private Operator target;
		private Operator amount;
		
		public Attack(Operator target, Operator amount) {
			this.target = target;
			this.amount = amount;
		}
		
		public void exec(Cell c) {
			// TODO implement Attack
		}
		
		public String toString() {
			return "Attack(" + target + "," + amount + ")";
		}
	}
}

package genome;

import cell.Cell;

public abstract class Term {
	// Should there be a children() method here?
	
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
	
	public static class Store extends Statement {
		private Operator a;
		private int i;
		
		public Store(int i, Operator a) {
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
	
	public static class Read extends Operator {
		private int a;
		
		public Read(int a) {
			this.a = a;
		}
		
		public int eval(Cell c) {
			return c.memGet(a);
		}
		
		public String toString() {
			return "$" + regName(a);
		}
	}
	
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
}

package genome;

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
		public void exec(ExecContext c) {
			// Do nothing
		}
	}

	public static abstract class Operator extends Term {
		public abstract int eval(ExecContext c);
	}
	
	public static class Nop extends Statement{
		public String toString() {
			return "Nop()";
		}
	}
	
	public static class Label extends Statement {
		private int label;
		
		public Label(int label) {
			this.label = label;
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
		
		public void exec(ExecContext c) {
			if (cond.eval(c) != 0) {
				c.labelJump(target);
			}
		}
		
		public String toString() {
			return "JumpLabel<"+target+">(" + cond + ")";
		}
	}
	
	public static class Print extends Statement {
		private Operator a;
		
		public Print(Operator a) {
			this.a = a;
		}
		
		public void exec(ExecContext c) {
			System.out.println("PRINT: " + a.eval(c));
		}
		
		public String toString() {
			return "Print("+a+")";
		}
	}
	
	public static class Store extends Statement {
		private Operator a;
		private int i;
		
		public Store(Operator a, int i) {
			this.a = a;
			this.i = i;
		}
		
		public void exec(ExecContext c) {
			c.memSet(i, a.eval(c));
		}
		
		public String toString() {
			return "$"+regName(i) + " <- " + a;
		}
	}
	
	public static class Read extends Operator {
		private int a;
		
		public Read(int a) {
			this.a = a;
		}
		
		public int eval(ExecContext c) {
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
		
		public int eval(ExecContext c) {
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
		
		public int eval(ExecContext c) {
			return a.eval(c) + b.eval(c);
		}
		
		public String toString() {
			return "Add(" + a + "," + b + ")";
		}
	}
	
	public static class LT extends Operator {
		private Operator a, b;
		
		public LT(Operator a, Operator b) {
			this.a = a;
			this.b = b;
		}
		
		public int eval(ExecContext c) {
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

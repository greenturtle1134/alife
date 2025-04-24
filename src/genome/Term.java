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
		public abstract void exec(CellContext c);
	}

	public static abstract class Operator extends Term {
		public abstract int eval(CellContext c);
	}
	
	public static class Nop extends Statement{
		public Nop() {
			// Nah
		}
		
		public void exec(CellContext c) {
			// Nope
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
		
		public void exec(CellContext c) {
			// Nope
		}
		
		public String toString() {
			return "Label<"+label+">";
		}
	}
	
	public static class Print extends Statement {
		private Operator a;
		
		public Print(Operator a) {
			this.a = a;
		}
		
		public void exec(CellContext c) {
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
		
		public void exec(CellContext c) {
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
		
		public int eval(CellContext c) {
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
		
		public int eval(CellContext c) {
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
		
		public int eval(CellContext c) {
			return a.eval(c) + b.eval(c);
		}
		
		public String toString() {
			return "Add(" + a + "," + b + ")";
		}
	}
}

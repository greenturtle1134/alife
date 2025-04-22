package genome;

public abstract class Term {
	// Should there be a children() method here?
	
	public static abstract class Statement extends Term {
		public abstract void exec(CellContext c);
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

	public static abstract class Operator extends Term {
		public abstract int eval(CellContext c);
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

package genome;

import java.util.Map;

public class ExecContext {
	public TestCell c;
	public int i;
	public Map<Integer, Integer> labels;
	
	public ExecContext (TestCell c, int i, Map<Integer, Integer> labels) {
		this.c = c;
		this.i = i;
		this.labels = labels;
	}

	public int memGet(int i) {
		return c.memGet(i);
	}

	public void memSet(int i, int x) {
		c.memSet(i, x);
	}
	
	public void jump(int i) {
		this.i = i-1;
	}
	
	public void labelJump(int label) {
		if (labels.containsKey(label)) {
			this.i = labels.get(label);
		}
	}
}

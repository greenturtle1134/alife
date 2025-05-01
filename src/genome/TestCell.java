package genome;

public class TestCell {
	private int[] memory;
	
	public TestCell() {
		// Dummy implementation for now
		this.memory = new int[65];
	}

	public int memGet(int i) {
		if (i < memory.length) {
			return memory[i];
		}
		else {
			return 0;
		}
	}
	
	public void memSet(int i, int x) {
		if (i < memory.length) {
			memory[i] = x;
		}
	}
}

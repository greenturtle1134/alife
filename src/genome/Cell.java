package genome;

public class Cell implements CellContext{
	private int[] memory;
	
	public Cell() {
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

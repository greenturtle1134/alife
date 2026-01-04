package cell;

/**
 * Represents the "internal values" of a cell, fully under control of its program
 */
public class CellInternals {
	public int[] memory;
	public double moveF;
	public double moveR;

	public CellInternals(int[] memory, double moveF, double moveR) {
		super();
		this.memory = memory;
		this.moveF = moveF;
		this.moveR = moveR;
	}
	
	public static CellInternals genDefault() {
		return new CellInternals(new int[64], 0, 0);
	}
}

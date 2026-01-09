package cell;

/**
 * Represents the "physical state" of the cell, comprising things like energy and size.
 */
public class CellState {
	public double nrg;
	public double body;
	
	public CellState(double nrg, double body) {
		this.nrg = nrg;
		this.body = body;
	}
}

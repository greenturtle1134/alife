package mutation;

import genome.DNA;

/**
 * A mutation replacing a block of nucleotides with another.
 */
public class BlockMutation extends Mutation {
	private int start;
	private DNA vals;

	public int getStart() {
		return start;
	}

	public DNA getVals() {
		return vals;
	}

	public BlockMutation(int start, DNA vals) {
		this.start = start;
		this.vals = vals;
	}

	@Override
	public DNA apply(DNA x) {
		return DNA.join(x.substring(0, start), vals, x.substring(start + vals.length()));
	}

	public String toString() {
		return "Replacement of " + vals + " starting at " + start;
	}
}

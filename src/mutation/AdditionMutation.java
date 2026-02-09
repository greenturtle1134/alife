package mutation;

import genome.DNA;

/**
 * A mutation adding a string of nucleotides. "start" determines the index that the first new nucleotide will have
 */
public class AdditionMutation extends Mutation {
	private int start;
	private DNA vals;

	public int getStart() {
		return start;
	}

	public DNA getVals() {
		return vals;
	}

	public AdditionMutation(int start, DNA vals) {
		this.start = start;
		this.vals = vals;
	}

	@Override
	public DNA apply(DNA x) {
		return DNA.join(x.substring(0, start), vals, x.substring(start));
	}
	
	public String toString() {
		return "Addition of " + vals + " at " + start;
	}

}

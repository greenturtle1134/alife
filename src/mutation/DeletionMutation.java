package mutation;

import genome.DNA;

/**
 * A mutation deleting a set of nucleotides, inclusive on start, exclusive on end
 */
public class DeletionMutation extends Mutation {
	private int start, end;

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public DeletionMutation(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public DNA apply(DNA x) {
		return DNA.join(x.substring(0, start), x.substring(end));
	}

	
	public String toString() {
		return "Deletion from " + start + " to " + end;
	}
}

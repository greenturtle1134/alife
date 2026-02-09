package genome;

import mutation.Mutation;

/**
 * Represents both the DNA and compiled program object. Could potentially use for optimized implementations or adding more tracking functionality later
 *
 */
public class Genome {
	private DNA dna;
	private Program program;
	
	public Genome(DNA dna) {
		this.dna = dna;
		this.program = Program.parseProgram(dna);
	}

	public DNA getDna() {
		return dna;
	}

	public Program getProgram() {
		return program;
	}
	
	/**
	 * Applies a mutation. Allows m to be null, in which case it is expected to return itself unmodified.
	 * @param m - the mutation
	 * @return the modified Genome
	 */
	public Genome applyMutation(Mutation m) {
		if (m == null) {
			return this;
		}
		else {
			return new Genome(m.apply(dna));
		}
	}
}

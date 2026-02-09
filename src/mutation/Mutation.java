package mutation;

import genome.DNA;

public abstract class Mutation {
	public abstract DNA apply(DNA x);
}

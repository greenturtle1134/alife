package mutation;

import java.util.random.RandomGenerator;

import genome.DNA;

public class MutationGenerator {
	private double pBlock, pAdd, pDel;
	private RandomGenerator rng;
	
	public MutationGenerator() {
		this.pBlock = 0.1;
		this.pAdd = 0.1;
		this.pDel = 0.1;
		this.rng = RandomGenerator.getDefault();
	}
	
	public Mutation generateMutation(DNA dna) {
		// TODO mutation probability calc
		if (rng.nextDouble() < pBlock) {
			int blockLen = rng.nextInt(1, 5); // TODO block length calc
			int start = rng.nextInt(dna.length() - blockLen);
			return new BlockMutation(start, DNA.randomUnequal(dna.substring(start, start + blockLen), rng));
		}
		else if (rng.nextDouble() < pAdd) {
			int start = rng.nextInt(dna.length()+1);
			int blockLen = rng.nextInt(1, 5); // TODO block length calc
			return new AdditionMutation(start, DNA.random(blockLen, rng));
		}
		else if (rng.nextDouble() < pDel) {
			int blockLen = rng.nextInt(1, 5); // TODO block length calc
			int start = rng.nextInt(dna.length() - blockLen);
			return new DeletionMutation(start, start + blockLen);
		}
		else {
			return null;
		}
	}
}

package mutation;

import java.util.random.RandomGenerator;

import genome.DNA;

public class MutationGenerator {
	private double pBlock, pAdd, pDel;
	private RandomGenerator rng;
	
	public MutationGenerator() {
		this.pBlock = 0.01;
		this.pAdd = 0.01;
		this.pDel = 0.01;
		this.rng = RandomGenerator.getDefault();
	}
	
	public Mutation generateMutation(DNA dna) {
		// TODO might want a more sophisticated mutation algorithm eventually
		if (rng.nextDouble() < pBlock) {
			int blockLen = rng.nextInt(1, 5);
			int start = rng.nextInt(dna.length() - blockLen);
			return new BlockMutation(start, DNA.randomUnequal(dna.substring(start, start + blockLen), rng));
		}
		else if (rng.nextDouble() < pAdd) {
			int start = rng.nextInt(dna.length()+1);
			int blockLen = rng.nextInt(1, 5);
			return new AdditionMutation(start, DNA.random(blockLen, rng));
		}
		else if (rng.nextDouble() < pDel) {
			int blockLen = rng.nextInt(1, 5);
			int start = rng.nextInt(dna.length() - blockLen);
			return new DeletionMutation(start, start + blockLen);
		}
		else {
			return null;
		}
	}
}

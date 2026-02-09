package genome;

import mutation.Mutation;
import mutation.MutationGenerator;

public class GenomeTest {

	public static void main(String[] args) {
		System.out.println("Testing mutation...");
		
		DNA test1 = DNA.stringToDNA("ATGATATATAG");
		System.out.println("Original:\n" + test1);
		System.out.println();
		
		MutationGenerator gen = new MutationGenerator();
		for (int i = 0; i<20; i++) {
			Mutation m = gen.generateMutation(test1);
			if (m != null) {
				DNA test2 = m.apply(test1);
				System.out.println(m);
				System.out.println(test2);
			}
			else {
				System.out.println("Nothing.");
			}
		}
	}

}

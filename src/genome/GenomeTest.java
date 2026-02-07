package genome;

public class GenomeTest {

	public static void main(String[] args) {
		System.out.println("Testing DNA join...");
		
		DNA test1 = DNA.stringToDNA("ATGATA");
		DNA test2 = DNA.stringToDNA("TATAG");
		
		System.out.println(test1.substring(3));
		System.out.println(test2.substring(1, 2));
		System.out.println(test2.substring(2, 5));
		
		System.out.println(DNA.join(test1, test2));
		DNA[] inputs = {test1.substring(3), test2.substring(1, 2), test2.substring(2, 5)};
		System.out.println(DNA.joinAll(inputs));
	}

}

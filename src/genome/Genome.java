package genome;

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
}

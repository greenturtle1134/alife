package genome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import genome.Term.Statement;

public class Program {
	private Statement[] statements;
	private Map<Integer, Integer> labels;
	private TreeSet<Integer> unskips;
	
	public Statement[] getStatements() {
		return this.statements;
	}
	
	public Map<Integer, Integer> getLabels() {
		return this.labels;
	}
	
	public TreeSet<Integer> getUnskips() {
		return this.unskips;
	}
	
	private Program(Statement[] statements, Map<Integer, Integer> labels, TreeSet<Integer> unskips) {
		this.statements = statements;
		this.labels = labels;
		this.unskips = unskips;
	}
	
	public String toString() {
		return Arrays.toString(this.statements);
	}
	
	public static Program parseProgram(DNA dna) {
		// Splice DNA and create parser
		byte[] codons = dna.makeSplices(dna.findSplices(DNA.TATA));
		Parser parser = new Parser(codons);
		
		// Parse statements one by one
		Statement current = parser.nextStatement();
		ArrayList<Statement> statementList = new ArrayList<Statement>();
		while (current != null) {
			statementList.add(current);
			current = parser.nextStatement();
		}
		Statement[] statements = statementList.toArray(new Statement[statementList.size()]);
		
		// Locate labels
		Map<Integer, Integer> labels = new HashMap<Integer, Integer>();
		for (int i = 0; i<statements.length; i++) {
			if (statements[i] instanceof Term.Label) {
				Term.Label label = (Term.Label) statements[i];
				labels.put(label.getLabel(), i);
			}
		}
		
		// Locate unskips
		TreeSet<Integer> unskips = new TreeSet<>();
		for (int i = 0; i<statements.length; i++) {
			if (statements[i] instanceof Term.Unskip) {
				unskips.add(i);
			}
		}
		// Add effective unskips at start and end
		unskips.add(-1);
		unskips.add(statements.length-1);
		
		return new Program(statements, labels, unskips);
	}
}

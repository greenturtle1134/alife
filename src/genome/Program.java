package genome;

import java.util.ArrayList;
import java.util.TreeMap;

import genome.Term.Statement;

public class Program {
	private Statement[] statements;
	private TreeMap<Integer, Integer> labels;
	
	public Statement[] getStatements() {
		return this.statements;
	}
	
	public TreeMap<Integer, Integer> getLabels() {
		return this.labels;
	}
	
	private Program(Statement[] statements, TreeMap<Integer, Integer> labels) {
		this.statements = statements;
		this.labels = labels;
	}
	
	public static Program parseProgram(DNA dna) {
		// Splice DNA and create parser
		byte[] codons = Parser.stripBlanks(dna.makeSplices(dna.findSplices()));
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
		TreeMap<Integer, Integer> labels = new TreeMap<Integer, Integer>();
		for (int i = 0; i<statements.length; i++) {
			if (statements[i] instanceof Term.Label) {
				Term.Label label = (Term.Label) statements[i];
				labels.put(label.getLabel(), i);
			}
		}
		
		return new Program(statements, labels);
	}
	
	public void run(CellContext c, ExecContext e) {
		// TODO The main feature not implemented here is jumping
		for (Statement s : statements) {
			s.exec(c);
		}
	}
}

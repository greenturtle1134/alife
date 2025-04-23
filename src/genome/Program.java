package genome;

import java.util.ArrayList;

import genome.Term.Statement;

public class Program {
	private Statement[] statements;
	
	public Statement[] getStatements() {
		return this.statements;
	}
	
	private Program(Statement[] statements) {
		this.statements = statements;
	}
	
	public static Program parseProgram(DNA dna) {
		byte[] codons = dna.makeSplices(dna.findSplices());
		Parser parser = new Parser(codons);
		
		Statement current = parser.nextStatement();
		ArrayList<Statement> statementList = new ArrayList<Statement>();
		while (current != null) {
			statementList.add(current);
			current = parser.nextStatement();
		}
		Statement[] statements = statementList.toArray(new Statement[statementList.size()]);
		
		return new Program(statements);
	}
	
	public void run(CellContext c, ExecContext e) {
		// TODO The main feature not implemented here is jumping
		for (Statement s : statements) {
			s.exec(c);
		}
	}
}

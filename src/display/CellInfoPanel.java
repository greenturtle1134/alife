package display;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cell.Cell;
import cell.Substance;
import genome.Term.Statement;

public class CellInfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Substance[] toDisplay;
	private JTextField[] substanceTexts;
	private JTextArea dnaText;
	private JTextArea programText;
	
	public CellInfoPanel(Substance[] toDisplay) {
		this.toDisplay = toDisplay;
		int n = toDisplay.length;
		this.substanceTexts = new JTextField[n];
		
		this.setLayout(new GridBagLayout());
		Insets insets = new Insets(1, 1, 1, 1);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = insets;
		c.anchor = GridBagConstraints.LINE_END;
		for (int i=0; i<n; i++) {
			JLabel label = new JLabel(toDisplay[i].shortName);
			JTextField textField = new JTextField();
			this.substanceTexts[i] = textField;
			textField.setEditable(false);
			textField.setPreferredSize(new Dimension(100, 20));
			
			c.gridy = i;
			
			c.gridx = 0;
			c.weightx = 0.0;
			c.fill = GridBagConstraints.NONE;
			this.add(label, c);
			
			c.gridx = 1;
			c.weightx = 1.0;
			c.fill = GridBagConstraints.HORIZONTAL;
			this.add(textField, c);
		}
		
		this.dnaText = new JTextArea();
		dnaText.setPreferredSize(new Dimension(100, 100));
		dnaText.setEditable(false);
		dnaText.setLineWrap(true);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = n;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = insets;
		this.add(dnaText, c);
		
		this.programText = new JTextArea();
		programText.setPreferredSize(new Dimension(100, 100));
		programText.setEditable(false);
		programText.setLineWrap(true);
		c.gridy = n+1;
		this.add(programText, c);
		
	}

	public void update(Cell c) {
		if (c != null) {
			// Cell exists, do update
			for (int i=0; i<substanceTexts.length; i++) {
				substanceTexts[i].setText(String.format("%.2f",c.getSubstance(toDisplay[i].id)));
			}
			dnaText.setText(c.getDna().toString());
			StringBuilder programString = new StringBuilder();
			Statement[] statements = c.getProgram().getStatements();
			for (int i = 0; i<statements.length; i++) {
				programString.append(statements[i].toString());
				if (i == c.getI()) {
					programString.append(" <--");
				}
				if (i < statements.length - 1) {
					programString.append("\n");
				}
			}
			programText.setText(programString.toString());
		}
		else {
			// Signals to clear the output
			for (int i=0; i<substanceTexts.length; i++) {
				substanceTexts[i].setText("");
			}
			dnaText.setText("");
			programText.setText("");
		}
	}
}

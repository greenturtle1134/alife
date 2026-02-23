package display;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cell.Cell;
import cell.Substance;

public class CellInfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Substance[] toDisplay;
	private JTextField[] textFields;
	
	public CellInfoPanel(Substance[] toDisplay) {
		this.toDisplay = toDisplay;
		int n = toDisplay.length;
		this.textFields = new JTextField[n];
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.LINE_END;
		for (int i=0; i<n; i++) {
			JLabel label = new JLabel(toDisplay[i].shortName);
			JTextField textField = new JTextField();
			this.textFields[i] = textField;
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
	}

	public void update(Cell c) {
		if (c != null) {
			for (int i=0; i<textFields.length; i++) {
				textFields[i].setText(String.format("%.2f",c.getSubstance(toDisplay[i].id)));
			}
		}
		else {
			for (int i=0; i<textFields.length; i++) {
				textFields[i].setText("");
			}
		}
	}
}

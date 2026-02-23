package display;

import cell.Cell;

/**
 * Represents an object that listens for status updates from a DisplayPanel. Used to move information to the other application components.
 */
public interface PanelListener {
	public void setStatus(String s);
	public void cellClicked(Cell c);
	
	public static PanelListener NULL = new PanelListener() {
		public void setStatus(String s) {};
		public void cellClicked(Cell c) {};
	};
}

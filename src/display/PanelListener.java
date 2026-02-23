package display;

import cell.Cell;

/**
 * Represents an object that listens for status updates from a DisplayPanel. Used to move information to the other application components.
 * In practice this just means Application for now but there may be more later.
 */
public interface PanelListener {
	public void setStatus(String s);
	public void cellClicked(Cell c);
	
	public static PanelListener NULL = new PanelListener() {
		public void setStatus(String s) {};
		public void cellClicked(Cell c) {};
	};
}

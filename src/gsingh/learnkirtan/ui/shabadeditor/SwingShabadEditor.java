package gsingh.learnkirtan.ui.shabadeditor;

import javax.swing.Action;
import javax.swing.JComponent;

public abstract class SwingShabadEditor extends JComponent implements
		ShabadEditor {

	protected boolean modified = false;

	public abstract void setEnabled(boolean bool);

	public abstract void setEditable(boolean bool);

	public abstract void reset();
	
	public abstract Action getUndoAction();

	public abstract Action getRedoAction();

	public boolean isModified() {
		return modified;
	}
}

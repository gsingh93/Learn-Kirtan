package gsingh.learnkirtan.ui.shabadeditor;

import javax.swing.Action;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public abstract class SwingShabadEditor extends JComponent implements
		ShabadEditor {

	protected boolean modified = false;

	public abstract void setEnabled(boolean bool);

	public abstract void setEditable(boolean bool);

	public abstract void reset();
	
	// TODO maybe just return an action factory?
	public abstract Action getUndoAction();

	public abstract Action getRedoAction();

	public abstract Action getCutAction();

	public abstract Action getCopyAction();

	public abstract Action getPasteAction();

	public boolean isModified() {
		return modified;
	}
}

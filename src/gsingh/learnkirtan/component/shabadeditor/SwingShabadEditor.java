package gsingh.learnkirtan.component.shabadeditor;

import gsingh.learnkirtan.component.shabadeditor.TextAreaShabadEditor.RedoAction;
import gsingh.learnkirtan.component.shabadeditor.TextAreaShabadEditor.UndoAction;

public interface SwingShabadEditor extends ShabadEditor {

	public void setEnabled(boolean bool);

	public void setEditable(boolean bool);

	public void reset();

	public UndoAction getUndoAction();

	public RedoAction getRedoAction();

}

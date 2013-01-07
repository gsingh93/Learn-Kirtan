package gsingh.learnkirtan.component.shabadeditor;

import gsingh.learnkirtan.component.shabadeditor.TextAreaShabadEditor.RedoAction;
import gsingh.learnkirtan.component.shabadeditor.TextAreaShabadEditor.UndoAction;
import gsingh.learnkirtan.shabad.Shabad;

import javax.swing.JTable;

@SuppressWarnings("serial")
public class TableShabadEditor extends JTable implements SwingShabadEditor {

	@Override
	public Shabad getShabad() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEditable(boolean bool) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public UndoAction getUndoAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RedoAction getRedoAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isModified() {
		// TODO Auto-generated method stub
		return false;
	}
}

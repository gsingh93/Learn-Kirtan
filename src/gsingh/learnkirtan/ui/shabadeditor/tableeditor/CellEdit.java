package gsingh.learnkirtan.ui.shabadeditor.tableeditor;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

@SuppressWarnings("serial")
public class CellEdit extends AbstractUndoableEdit {
	protected UndoTableModel tableModel;
	protected Object oldValue;
	protected Object newValue;
	protected int row;
	protected int column;

	public CellEdit(UndoTableModel tableModel, Object oldValue,
			Object newValue, int row, int column) {
		this.tableModel = tableModel;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.row = row;
		this.column = column;
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();

		tableModel.setValueAt(oldValue, row, column, false);
	}

	@Override
	public void redo() throws CannotUndoException {
		super.redo();

		tableModel.setValueAt(newValue, row, column, false);
	}

}

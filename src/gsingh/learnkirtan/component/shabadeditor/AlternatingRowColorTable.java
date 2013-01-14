package gsingh.learnkirtan.component.shabadeditor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class AlternatingRowColorTable extends JTable {
	private EditUndoManager undoManager;
	public AlternatingRowColorTable(int rows, int cols) {
		super(new UndoTableModel());
		undoManager = new EditUndoManager();
		UndoTableModel model = (UndoTableModel) getModel();
		model.addUndoableEditListener(undoManager);
		model.setRowCount(rows);
		model.setColumnCount(cols);
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row,
			int col) {
		Component c = super.prepareRenderer(renderer, row, col);
		if (!c.getBackground().equals(getSelectionBackground())) {
			if (row % 2 == 1) {
				c.setBackground(Color.LIGHT_GRAY);
			} else {
				c.setBackground(Color.WHITE);
			}
		}
		return c;
	}
	
	public Action getUndoAction() {
		return undoManager.getUndoAction();
	}

	public Action getRedoAction() {
		return undoManager.getRedoAction();
	}
}

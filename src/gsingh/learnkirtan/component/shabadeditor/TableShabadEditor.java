package gsingh.learnkirtan.component.shabadeditor;

import gsingh.learnkirtan.WindowTitleManager;
import gsingh.learnkirtan.parser.Parser;
import gsingh.learnkirtan.shabad.Shabad;

import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class TableShabadEditor extends SwingShabadEditor {

	private JTable table = new JTable(16, 16);

	private DefaultTableModel model = (DefaultTableModel) table.getModel();


	public TableShabadEditor(WindowTitleManager titleManager) {
		super(titleManager);

		Integer[] headers = new Integer[16];
		for (int i = 0; i < 16; i++) {
			headers[i] = i + 1;
		}

		table.setRowHeight(20);
		model.setColumnIdentifiers(headers);

		setLayout(new GridLayout());
		add(new JScrollPane(table));
	}

	// TODO Set renderer
	// @Override
	// public Component prepareRenderer(TableCellRenderer renderer, int row,
	// int col) {
	// Component c = table.prepareRenderer(renderer, row, col);
	// if (!c.getBackground().equals(getSelectionBackground())) {
	// if (row % 2 == 1) {
	// c.setBackground(Color.LIGHT_GRAY);
	// } else {
	// c.setBackground(Color.WHITE);
	// }
	// }
	// return c;
	// }

	@Override
	public Shabad getShabad() {
		Parser parser = new Parser();
		Shabad shabad = parser.parse(getText());
		return shabad;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getText() {
		int numRows = model.getRowCount();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < numRows; i += 2) {
			for (int j = 0; j < 16; j++) {
				String value = (String) model.getValueAt(i, j);
				if (value != null) {
					sb.append(model.getValueAt(i, j)).append(" ");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void setEditable(boolean bool) {
		setEditable(bool);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isModified() {
		return modified;
	}

	/**
	 * Inserts a pair of rows into the table
	 * 
	 * @param row
	 *            the row to insert the pair above
	 */
	public void addRowAbove(int row) {
		model.insertRow(row, new Object[] {});
		model.insertRow(row, new Object[] {});
	}

	public void addRowBelow(int row) {
		// TODO
	}

	/**
	 * Deletes the specified row from the table. Also deletes the other row in
	 * the pair
	 * 
	 * @param row
	 *            the row number of the row to delete
	 */
	public void deleteRow(int row) {
		model.removeRow(row);
		if (row % 2 == 0) {
			model.removeRow(row);
		} else {
			model.removeRow(row - 1);
		}
	}

	@Override
	public void setEnabled(boolean bool) {
		// TODO Auto-generated method stub
	}

	public int getSelectedRow() {
		return table.getSelectedRow();
	}
}

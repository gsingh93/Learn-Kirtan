package gsingh.learnkirtan.ui.shabadeditor.tableeditor;

import gsingh.learnkirtan.parser.Parser;
import gsingh.learnkirtan.shabad.Shabad;
import gsingh.learnkirtan.shabad.ShabadMetaData;
import gsingh.learnkirtan.shabad.ShabadNotes;
import gsingh.learnkirtan.ui.WindowTitleManager;
import gsingh.learnkirtan.ui.shabadeditor.SwingShabadEditor;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class TableShabadEditor extends SwingShabadEditor {

	private AlternatingRowColorTable table;

	private UndoTableModel model;

	private ShabadMetaData metaData;

	public TableShabadEditor(final WindowTitleManager titleManager) {
		table = new AlternatingRowColorTable(16, 16, titleManager);
		model = (UndoTableModel) table.getModel();
		Integer[] headers = new Integer[16];
		for (int i = 0; i < 16; i++) {
			headers[i] = i + 1;
		}

		table.setSelectAllForEdit(true);
		table.setCellSelectionEnabled(true);
		table.setRowHeight(20);
		table.setFont(new Font("Arial", Font.PLAIN, 20));
		model.setColumnIdentifiers(headers);
		table.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < 16; i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setCellRenderer(renderer);
		}

		setLayout(new GridLayout());
		add(new JScrollPane(table));

		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent event) {
				modified = true;
				titleManager.setDocumentModifiedTitle();
			}
		});

		metaData = new ShabadMetaData();
	}

	@Override
	public Action getUndoAction() {
		return table.getUndoAction();
	}

	@Override
	public Action getRedoAction() {
		return table.getRedoAction();
	}

	@Override
	public Shabad getShabad() {
		Shabad shabad = new Shabad(getNotesString(), getWords());
		shabad.setMetaData(metaData);
		return shabad;
	}

	@Override
	public void setShabad(Shabad shabad) {
		metaData = shabad.getMetaData();
		setNotes(shabad.getNotes());
		setWords(shabad.getWords());
	}

	private void setNotes(ShabadNotes notes) {
		int numRows = model.getRowCount();
		for (int i = 1; i < numRows; i += 2) {
			for (int j = 0; j < 16; j++) {
				int index = i / 2 * 16 + j;
				String word;
				if (index >= notes.size())
					word = "null";
				else
					word = notes.get(index).getNoteText();
				if (!word.equalsIgnoreCase("null")) {
					// TODO: Titlecase word
					table.setValueAt(word, i, j);
				} else {
					table.setValueAt(null, i, j);
				}
			}
		}
	}

	@Override
	public ShabadNotes getNotes() {
		Parser parser = new Parser();
		return parser.parse(getNotesString());
	}

	private String getNotesString() {
		int numRows = model.getRowCount();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < numRows; i += 2) {
			for (int j = 0; j < 16; j++) {
				sb.append(model.getValueAt(i, j)).append(" ");
			}
		}
		return sb.toString();
	}

	public String getWords() {
		int numRows = model.getRowCount();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numRows; i += 2) {
			for (int j = 0; j < 16; j++) {
				sb.append(model.getValueAt(i, j)).append(" ");
			}
		}
		return sb.toString();
	}

	public boolean isValidShabad() {
		return table.isValidShabad();
	}

	public void setWords(String text) {
		String[] words = text.split("\\s+");
		int numRows = model.getRowCount();
		for (int i = 0; i < numRows; i += 2) {
			for (int j = 0; j < 16; j++) {
				int index = i / 2 * 16 + j;
				String word;
				if (index >= words.length)
					word = "null";
				else
					word = words[index];
				if (!word.equalsIgnoreCase("null")) {
					// TODO: Titlecase word
					table.setValueAt(word, i, j);
				} else {
					table.setValueAt(null, i, j);
				}
			}
		}
	}

	@Override
	public void setEditable(boolean bool) {
		setEditable(bool);
	}

	@Override
	public void reset() {
		modified = false;
		table.reset();
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
		table.setEnabled(bool);
	}

	public int getSelectedRow() {
		return table.getSelectedRow();
	}

	@Override
	public ShabadMetaData getMetaData() {
		return metaData;
	}

	@Override
	public void setMetaData(ShabadMetaData metaData) {
		this.metaData = metaData;
	}
}

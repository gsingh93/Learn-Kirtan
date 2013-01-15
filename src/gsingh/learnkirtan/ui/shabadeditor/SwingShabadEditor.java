package gsingh.learnkirtan.ui.shabadeditor;

import gsingh.learnkirtan.WindowTitleManager;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public abstract class SwingShabadEditor extends JComponent implements
		ShabadEditor,
		UndoableEditListener {

	protected boolean modified = false;

	private UndoManager undoManager = new UndoManager();
	private UndoAction undoAction = new UndoAction();
	private RedoAction redoAction = new RedoAction();

	public abstract void setEnabled(boolean bool);

	public abstract void setEditable(boolean bool);

	public abstract void reset();

	private WindowTitleManager titleManager;

	public SwingShabadEditor(WindowTitleManager titleManager) {
		this.titleManager = titleManager;
	}

	public abstract boolean isValidShabad();

	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		// Save the edit and update the menus
		undoManager.addEdit(e.getEdit());
		undoAction.updateUndoState();
		redoAction.updateRedoState();

		updateTitleOnChange();
	}

	private void updateTitleOnChange() {
		if (!undoManager.canUndo()) {
			titleManager.setDocumentUnmodifiedTitle();
			modified = false;
		} else {
			titleManager.setDocumentModifiedTitle();
			modified = true;
		}
	}

	public Action getUndoAction() {
		return undoAction;
	}

	public Action getRedoAction() {
		return redoAction;
	}

	public boolean isModified() {
		return modified;
	}

	public class UndoAction extends AbstractAction {

		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undoManager.undo();
			} catch (CannotUndoException ex) {
				ex.printStackTrace();
			}
			updateUndoState();
			redoAction.updateRedoState();
		}

		public void updateUndoState() {
			if (undoManager.canUndo()) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}

	public class RedoAction extends AbstractAction {

		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undoManager.redo();
			} catch (CannotRedoException ex) {
				ex.printStackTrace();
			}
			updateRedoState();
			undoAction.updateUndoState();
		}

		public void updateRedoState() {
			if (undoManager.canRedo()) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}
}

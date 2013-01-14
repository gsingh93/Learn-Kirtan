package gsingh.learnkirtan.component.shabadeditor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class EditUndoManager extends UndoManager {
	protected Action undoAction;
	protected Action redoAction;

	public EditUndoManager() {
		this.undoAction = new UndoAction(this);
		this.redoAction = new RedoAction(this);

		synchronizeActions(); // to set initial names
	}

	public Action getUndoAction() {
		return undoAction;
	}

	public Action getRedoAction() {
		return redoAction;
	}
	
	@Override
	public boolean addEdit(UndoableEdit anEdit) {
		try {
			return super.addEdit(anEdit);
		} finally {
			synchronizeActions();
		}
	}
	
	@Override
	protected void undoTo(UndoableEdit edit) throws CannotUndoException {
		try {
			super.undoTo(edit);
		} finally {
			synchronizeActions();
		}
	}
	
	@Override
	protected void redoTo(UndoableEdit edit) throws CannotRedoException {
		try {
			super.redoTo(edit);
		} finally {
			synchronizeActions();
		}
	}
	
	protected void synchronizeActions() {
		undoAction.setEnabled(canUndo());
		undoAction.putValue(Action.NAME, getUndoPresentationName());

		redoAction.setEnabled(canRedo());
		redoAction.putValue(Action.NAME, getRedoPresentationName());
	}
}

class UndoAction extends AbstractAction {
	protected final UndoManager manager;

	public UndoAction(UndoManager manager) {
		this.manager = manager;
		}

	public void actionPerformed(ActionEvent e) {
		try {
			manager.undo();
		} catch (CannotUndoException ex) {
			ex.printStackTrace();
		}
	}
}

class RedoAction extends AbstractAction {
	protected final UndoManager manager;

	public RedoAction(UndoManager manager) {
		this.manager = manager;
		}

	public void actionPerformed(ActionEvent e) {
		try {
			manager.redo();
		} catch (CannotRedoException ex) {
			ex.printStackTrace();
		}
	}
}


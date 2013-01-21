package gsingh.learnkirtan.ui.shabadeditor;

import gsingh.learnkirtan.Constants;
import gsingh.learnkirtan.WindowTitleManager;
import gsingh.learnkirtan.keys.KeyMapper;
import gsingh.learnkirtan.keys.LabelManager;
import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.note.NoteList;
import gsingh.learnkirtan.parser.Parser;
import gsingh.learnkirtan.player.ShabadPlayer;
import gsingh.learnkirtan.shabad.Shabad;
import gsingh.learnkirtan.shabad.ShabadNotes;
import gsingh.learnkirtan.utility.Utility;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

@SuppressWarnings("serial")
public class TextAreaShabadEditor extends SwingShabadEditor implements
		DocumentListener, UndoableEditListener, KeyListener {

	public enum Mode {
		COMPOSE, PLAY
	}

	private Mode mode;

	private WindowTitleManager titleManager;

	private JTextArea textArea = new JTextArea(16, 57);

	private UndoManager undoManager = new UndoManager();
	private UndoAction undoAction = new UndoAction();
	private RedoAction redoAction = new RedoAction();

	private boolean modified = false;

	private LabelManager labelManager;

	public TextAreaShabadEditor(WindowTitleManager titleManager,
			LabelManager labelManager) {
		this.titleManager = titleManager;
		this.labelManager = labelManager;

		textArea.setFont(new Font("Dialog", Font.BOLD, 16));
		Document document = textArea.getDocument();
		document.addDocumentListener(this);
		document.addUndoableEditListener(this);
	}

	@Override
	public Shabad getShabad() {
		Parser parser = new Parser();
		return new Shabad(getText());
	}

	// @Override
	// public String getText() {
	// return super.getText();
	// }
	//
	// @Override
	// public void setText(String text) {
	// super.setText(text);
	// }

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// Empty for plain text components
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		updateTitleOnChange();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		updateTitleOnChange();
	}

	public boolean isValidShabad() {
		return true; // TODO
	}

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

	public void reset() {
		textArea.requestFocusInWindow();
		textArea.setCaretPosition(0);
		resetUndoManager();
	}

	public void resetUndoManager() {
		undoManager.discardAllEdits();
		undoAction.setEnabled(false);
	}

	// @Override
	// public void focusGained(FocusEvent e) {
	// getCaret().setVisible(true);
	// }

	@Override
	public void keyTyped(KeyEvent e) {
		if (!e.isAltDown() && !e.isControlDown()) {
			if (mode == Mode.COMPOSE) {
				int keyId = Utility.letterToKeyId(String
						.valueOf(e.getKeyChar()).toUpperCase(), labelManager
						.getOctave());
				KeyMapper keyMapper = KeyMapper.getInstance();
				NoteList notes = keyMapper.getNotes();
				if (keyId < Constants.MAX_KEYS && keyId >= 0) {
					final Note note = notes.getNoteFromKeyId(keyId);

					textArea.insert(note.getNoteText() + " ",
							textArea.getCaretPosition());
					new Thread(new Runnable() {
						public void run() {
							ShabadPlayer shabadPlayer = new ShabadPlayer(); // TODO
							shabadPlayer.playNote(note, 1);
						}
					}).start();
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public void setWords(String text) {
		// Does nothing
	}

	private String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEnabled(boolean bool) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEditable(boolean bool) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setShabad(Shabad shabad) {
		// TODO Auto-generated method stub

	}

	@Override
	public ShabadNotes getNotes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWords() {
		// TODO Auto-generated method stub
		return null;
	}

	public Action getUndoAction() {
		return undoAction;
	}

	public Action getRedoAction() {
		return redoAction;
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

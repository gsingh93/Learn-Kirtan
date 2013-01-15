package gsingh.learnkirtan.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

@SuppressWarnings("serial")
public class EditMenu extends JMenu {

	public EditMenu(Action undoAction, Action redoAction) {
		super("Edit");

		JMenuItem undoItem = new JMenuItem(undoAction);
		JMenuItem redoItem = new JMenuItem(redoAction);
		JMenuItem cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
		JMenuItem copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
		JMenuItem pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());

		undoItem.setMnemonic(KeyEvent.VK_U);
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				ActionEvent.CTRL_MASK));
		undoItem.setMnemonic(KeyEvent.VK_R);
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				ActionEvent.CTRL_MASK));
		cutItem.setText("Cut");
		copyItem.setText("Copy");
		pasteItem.setText("Paste");

		setMnemonic(KeyEvent.VK_D);
		add(undoItem);
		add(redoItem);
		add(cutItem);
		add(copyItem);
		add(pasteItem);
	}
}

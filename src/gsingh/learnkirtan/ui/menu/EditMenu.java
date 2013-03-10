package gsingh.learnkirtan.ui.menu;

import gsingh.learnkirtan.Keys;

import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class EditMenu extends JMenu {

	public EditMenu(Action undoAction, Action redoAction, Action cutAction,
			Action copyAction, Action pasteAction) {
		super("Edit");

		JMenuItem undoItem = new JMenuItem(undoAction);
		JMenuItem redoItem = new JMenuItem(redoAction);
		JMenuItem cutItem = new JMenuItem(cutAction);
		JMenuItem copyItem = new JMenuItem(copyAction);
		JMenuItem pasteItem = new JMenuItem(pasteAction);

		undoItem.setMnemonic(KeyEvent.VK_U);
		undoItem.setAccelerator(Keys.UNDO_KEY);
		undoItem.setMnemonic(KeyEvent.VK_R);
		redoItem.setAccelerator(Keys.REDO_KEY);
		cutItem.setText("Cut");
		cutItem.setAccelerator(Keys.CUT_KEY);
		copyItem.setText("Copy");
		copyItem.setAccelerator(Keys.COPY_KEY);
		pasteItem.setText("Paste");
		pasteItem.setAccelerator(Keys.PASTE_KEY);

		setMnemonic(KeyEvent.VK_D);
		add(undoItem);
		add(redoItem);
		add(cutItem);
		add(copyItem);
		add(pasteItem);
	}
}

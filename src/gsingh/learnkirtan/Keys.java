package gsingh.learnkirtan;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public class Keys {
	public static final KeyStroke SAVE_KEY = KeyStroke.getKeyStroke(
			KeyEvent.VK_S, ActionEvent.CTRL_MASK);

	public static final KeyStroke OPEN_KEY = KeyStroke.getKeyStroke(
			KeyEvent.VK_O, ActionEvent.CTRL_MASK);

	public static final KeyStroke NEW_KEY = KeyStroke.getKeyStroke(
			KeyEvent.VK_N, ActionEvent.CTRL_MASK);

	public static final KeyStroke PROPERTIES_KEY = KeyStroke.getKeyStroke(
			KeyEvent.VK_ENTER, ActionEvent.ALT_MASK);

	public static final KeyStroke CUT_KEY = KeyStroke.getKeyStroke(
			KeyEvent.VK_X, ActionEvent.CTRL_MASK);

	public static final KeyStroke COPY_KEY = KeyStroke.getKeyStroke(
			KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);

	public static final KeyStroke PASTE_KEY = KeyStroke.getKeyStroke(
			KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);

	public static final KeyStroke UNDO_KEY = KeyStroke.getKeyStroke(
			KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);

	public static final KeyStroke REDO_KEY = KeyStroke.getKeyStroke(
			KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);

	public static final KeyStroke DELETE_KEY = KeyStroke.getKeyStroke(
			KeyEvent.VK_DELETE, 0);
}

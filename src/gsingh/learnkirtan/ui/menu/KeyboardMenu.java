package gsingh.learnkirtan.ui.menu;

import gsingh.learnkirtan.controller.menu.KeyboardMenuController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class KeyboardMenu extends JMenu implements ActionListener {

	private KeyboardMenuController controller;

	public KeyboardMenu(KeyboardMenuController keyboardMenuController) {
		super("Keyboard Mode");
		this.controller = keyboardMenuController;

		JMenuItem composeItem = new JMenuItem("Compose", KeyEvent.VK_C);
		JMenuItem editItem = new JMenuItem("Edit", KeyEvent.VK_E);

		composeItem.setActionCommand("composemode");
		composeItem.addActionListener(this);
		composeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.ALT_MASK));
		editItem.setActionCommand("editmode");
		editItem.addActionListener(this);
		editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.ALT_MASK));

		setMnemonic(KeyEvent.VK_K);
		add(composeItem);
		add(editItem);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("composemode")) {
			controller.setComposeMode();
		} else if (command.equals("editmode")) {
			controller.setEditMode();
		}
	}
}

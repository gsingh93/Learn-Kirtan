package gsingh.learnkirtan.ui.menu;

import gsingh.learnkirtan.ui.menu.controller.FileMenuController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class FileMenu extends JMenu implements ActionListener {

	private FileMenuController controller;

	public FileMenu(FileMenuController controller) {
		super("File");
		this.controller = controller;

		JMenuItem createItem = new JMenuItem("Create new shabad", KeyEvent.VK_C);
		JMenuItem openItem = new JMenuItem("Open existing shabad",
				KeyEvent.VK_O);
		JMenuItem saveItem = new JMenuItem("Save current shabad", KeyEvent.VK_S);
		JMenuItem propertiesItem = new JMenuItem("File properties",
				KeyEvent.VK_P);

		createItem.setActionCommand("create");
		createItem.addActionListener(this);
		createItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		openItem.setActionCommand("open");
		openItem.addActionListener(this);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		saveItem.setActionCommand("save");
		saveItem.addActionListener(this);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		propertiesItem.setActionCommand("properties");
		propertiesItem.addActionListener(this);
		propertiesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
				ActionEvent.ALT_MASK));

		setMnemonic(KeyEvent.VK_F);
		add(createItem);
		add(openItem);
		add(saveItem);
		addSeparator();
		add(propertiesItem);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		// TODO Factor out hard coded strings
		if (command.equals("open")) {
			controller.open();
		} else if (command.equals("save")) {
			controller.save();
		} else if (command.equals("create")) {
			controller.create();
		} else if (command.equals("properties")) {
			controller.properties();
		} else {
			// TODO Properly exit
			System.out.println("Error");
		}
	}
}

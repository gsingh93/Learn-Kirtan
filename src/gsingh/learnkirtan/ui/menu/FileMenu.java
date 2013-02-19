package gsingh.learnkirtan.ui.menu;

import gsingh.learnkirtan.Keys;
import gsingh.learnkirtan.ui.action.ActionFactory;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class FileMenu extends JMenu {

	public FileMenu(ActionFactory actionFactory) {
		super("File");

		JMenuItem createItem = new JMenuItem("Create new shabad", KeyEvent.VK_C);
		JMenuItem openItem = new JMenuItem("Open existing shabad",
				KeyEvent.VK_O);
		JMenuItem saveItem = new JMenuItem("Save current shabad", KeyEvent.VK_S);
		JMenuItem propertiesItem = new JMenuItem("File properties",
				KeyEvent.VK_P);

		createItem.setAction(actionFactory.newCreateAction());
		createItem.setAccelerator(Keys.NEW_KEY);
		createItem.setText("New");
		openItem.setAction(actionFactory.newOpenAction());
		openItem.setAccelerator(Keys.OPEN_KEY);
		openItem.setText("Open");
		saveItem.setAction(actionFactory.newSaveAction());
		saveItem.setAccelerator(Keys.SAVE_KEY);
		saveItem.setText("Save");

		propertiesItem.setAction(actionFactory.newPropertiesAction());
		propertiesItem.setAccelerator(Keys.PROPERTIES_KEY);
		propertiesItem.setText("Properties");

		setMnemonic(KeyEvent.VK_F);
		add(createItem);
		add(openItem);
		add(saveItem);
		addSeparator();
		add(propertiesItem);
	}
}

package gsingh.learnkirtan.ui.menu;

import gsingh.learnkirtan.controller.menu.OptionsMenuController;
import gsingh.learnkirtan.settings.SettingsManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class OptionsMenu extends JMenu implements ActionListener, ItemListener {

	private OptionsMenuController controller;
	private JCheckBoxMenuItem showSargamLabelsItem = new JCheckBoxMenuItem(
			"Show Sargam Labels");
	private JCheckBoxMenuItem showKeyboardLabelsItem = new JCheckBoxMenuItem(
			"Show Keyboard Labels");

	public OptionsMenu(OptionsMenuController optionsMenuController) {
		super("Options");
		this.controller = optionsMenuController;
		SettingsManager settingsManager = SettingsManager.getInstance();

		JMenuItem saItem = new JMenuItem("Change Sa Key", KeyEvent.VK_C);
		showSargamLabelsItem.setSelected(settingsManager.getShowSargamLabels());
		showKeyboardLabelsItem.setSelected(settingsManager
				.getShowKeyboardLabels());

		saItem.setActionCommand("changesa");
		saItem.addActionListener(this);
		showSargamLabelsItem.setActionCommand("sargamlabels");
		showSargamLabelsItem.addItemListener(this);
		showKeyboardLabelsItem.setActionCommand("keyboardlabels");
		showKeyboardLabelsItem.addItemListener(this);

		setMnemonic(KeyEvent.VK_O);
		add(saItem);
		add(showSargamLabelsItem);
		add(showKeyboardLabelsItem);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("changesa")) {
			controller.changeSa();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		if (source == showSargamLabelsItem) {
			if (e.getStateChange() == ItemEvent.SELECTED)
				controller.showSargamLabels(true);
			else
				controller.showSargamLabels(false);
		} else if (source == showKeyboardLabelsItem) {
			if (e.getStateChange() == ItemEvent.SELECTED)
				controller.showKeyboardLabels(true);
			else
				controller.showKeyboardLabels(false);
		}

	}
}

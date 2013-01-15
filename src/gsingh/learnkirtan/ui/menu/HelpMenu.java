package gsingh.learnkirtan.ui.menu;

import gsingh.learnkirtan.controller.menu.HelpMenuController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class HelpMenu extends JMenu implements ActionListener {

	private HelpMenuController controller;

	public HelpMenu(HelpMenuController helpMenuController) {
		super("Help");
		this.controller = helpMenuController;

		JMenuItem helpItem = new JMenuItem("Help", KeyEvent.VK_H);
		JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);
		JMenuItem checkForUpdateItem = new JMenuItem("Check For Updates",
				KeyEvent.VK_C);

		// Create HelpSet and HelpBroker objects
		HelpSet hs = getHelpSet("LearnKirtanHelpSet.hs");
		HelpBroker hb = hs.createHelpBroker();

		// Assign help to components
		CSH.setHelpIDString(helpItem, "top");

		// Add action listeners
		helpItem.addActionListener(new CSH.DisplayHelpFromSource(hb));
		helpItem.setAccelerator(KeyStroke.getKeyStroke("F1"));

		aboutItem.setActionCommand("about");
		aboutItem.addActionListener(this);
		checkForUpdateItem.setActionCommand("checkforupdate");
		checkForUpdateItem.addActionListener(this);

		setMnemonic(KeyEvent.VK_H);
		add(helpItem);
		add(aboutItem);
		add(checkForUpdateItem);
	}

	/**
	 * Find the HelpSet file and create a HelpSet object
	 */
	private HelpSet getHelpSet(String helpSetFile) {
		HelpSet hs = null;
		ClassLoader cl = this.getClass().getClassLoader();
		try {
			URL hsURL = HelpSet.findHelpSet(cl, helpSetFile);
			hs = new HelpSet(null, hsURL);
		} catch (HelpSetException e) {
			e.printStackTrace();
		}
		return hs;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("about")) {
			controller.displayAbout();
		} else if (command.equals("checkforupdate")) {
			controller.checkForUpdate();
		}
	}
}

package gsingh.learnkirtan.ui.menu.controller;

import gsingh.learnkirtan.utility.DialogUtility;
import gsingh.learnkirtan.utility.NetworkUtility;

public class HelpMenuController {
	public HelpMenuController() {
	}

	public void displayAbout() {
		DialogUtility.showAboutDialog();
	}

	public void checkForUpdate() {
		if (!NetworkUtility.checkForUpdate())
			DialogUtility.showUpToDateDialog();
	}
}

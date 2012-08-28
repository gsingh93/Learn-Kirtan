package gsingh.learnkirtan.controller.menu;

import gsingh.learnkirtan.StateModel;
import gsingh.learnkirtan.utility.DialogUtility;
import gsingh.learnkirtan.utility.NetworkUtility;

public class HelpMenuController {
	public HelpMenuController(StateModel model) {
	}

	public void displayAbout() {
		DialogUtility.showAboutDialog();
	}

	public void checkForUpdate() {
		if (!NetworkUtility.checkForUpdate())
			DialogUtility.showUpToDateDialog();
	}
}

package gsingh.learnkirtan.utility;

import static gsingh.learnkirtan.Constants.VERSION;
import gsingh.learnkirtan.Constants.Duration;
import gsingh.learnkirtan.settings.SettingsManager;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A class for handling functionality requiring network access, such as checking
 * for updates
 * 
 * @author Gulshan
 * 
 */
public class NetworkUtility {

	private static JComboBox reminder;

	/** The URL for the text file that contains the number of the latest version */
	private static final String VERSION_URL = "http://michigangurudwara.com/version.txt";

	/** The URL of the site to go to if the user wants to update */
	private static final String UPDATE_SITE = "https://github.com/gsingh93/Learn-Kirtan/wiki";

	/** The value of the checkbox in the reminder dialog */
	private static boolean checked = false;

	/**
	 * Connects to the server to see if there is a later update. If found, offer
	 * to go to download page
	 * 
	 * @return true if we checked for an update, false otherwise
	 */
	public static boolean checkForUpdate() {
		SettingsManager settingsManager = SettingsManager.getInstance();

		// Check if the "Don't remind for X days" interval is finished
		settingsManager.checkReminderOffDurationReached();

		// If we're supposed to check for an update
		if (settingsManager.getCheckForUpdate()) {
			try {

				// Open a reader to the file containing the version information
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new URL(VERSION_URL).openStream()));
				String version = reader.readLine();

				// If we have an older version
				if (isOldVersion(version)) {
					JPanel panel = constructUpdateDialog();
					int result = DialogUtility.showUpdateAvailableDialog(panel);

					if (DialogUtility.isYes(result))
						Desktop.getDesktop().browse(new URI(UPDATE_SITE));

					// Turns the reminders on or off
					settingsManager.setCheckForUpdate(!checked,
							Duration.values()[reminder.getSelectedIndex()]);

					return true;
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * Constructs the dialog box to be shown to the user
	 * 
	 * @return the JPanel that will be in the dialog box
	 */
	private static JPanel constructUpdateDialog() {
		reminder = new JComboBox(new String[] { "Day", "Week", "Month" });

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(
				new JLabel(
						"The software has detected that an update is available. Would you like to go to the download page?"),
				BorderLayout.NORTH);

		JPanel reminderPanel = new JPanel();
		reminderPanel.add(new JLabel());

		JCheckBox checkBox = new JCheckBox();
		checkBox.addItemListener(new CheckBoxListener());

		reminderPanel.add(checkBox);
		reminderPanel.add(new JLabel("Don't remind me about updates for 1 "));
		reminderPanel.add(reminder);

		panel.add(reminderPanel, BorderLayout.SOUTH);
		return panel;
	}

	// TODO: Untested
	/**
	 * Checks to see if the current version of the software is not up to date
	 * 
	 * @param version
	 *            the latest version of the software
	 * 
	 * @return true if the current version is older than the latest version
	 */
	private static boolean isOldVersion(String version) {
		String[] currentVersion = VERSION.split("\\.");
		String[] latestVersion = version.split("\\.");

		// Make sure the version numbers have the same "length"
		if (currentVersion.length != latestVersion.length) {
			throw new IllegalArgumentException(
					"The number of separators in the latest version does not match the number of separators in the current version");
		}

		for (int i = 0; i < latestVersion.length; i++) {
			int cNum = Integer.valueOf(currentVersion[i]);
			int lNum = Integer.valueOf(latestVersion[i]);
			if (cNum < lNum)
				return true;
		}

		return false;
	}

	static class CheckBoxListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED)
				checked = true;
			else
				checked = false;
		}
	}
}

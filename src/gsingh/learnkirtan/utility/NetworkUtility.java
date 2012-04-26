package gsingh.learnkirtan.utility;

import gsingh.learnkirtan.Main;
import gsingh.learnkirtan.SettingsManager;

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
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class NetworkUtility {
	/**
	 * The logger for this class
	 */
	private final static Logger LOGGER = Logger.getLogger(NetworkUtility.class
			.getName());

	private static JComboBox reminder;

	private static final String VERSION_URL = "http://michigangurudwara.com/version.txt";

	private static final String UPDATE_SITE = "https://github.com/gsingh93/Learn-Kirtan/wiki";

	private static boolean checked = false;

	/**
	 * Connects to the server to see if there is a later update. If found, offer
	 * to go to download page
	 */
	public static void checkForUpdate(final String VERSION) {
		String line = VERSION;
		SettingsManager settingsManager = Main.getMain().getSettingsManager();
		if (settingsManager.getRemind()) {
			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new URL(VERSION_URL).openStream()));
				line = reader.readLine();

				if (!line.equals(VERSION)) {
					JPanel panel = constructUpdateDialog();
					int result = JOptionPane.showOptionDialog(null, panel,
							"Update Available", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, null, null);

					LOGGER.info("Update Available");

					if (result == JOptionPane.YES_OPTION)
						Desktop.getDesktop().browse(new URI(UPDATE_SITE));

					settingsManager.setRemind(!checked,
							reminder.getSelectedIndex());
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			settingsManager.checkReminderOffDurationReached();
		}
	}

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

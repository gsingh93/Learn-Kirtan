package gsingh.learnkirtan;

import static gsingh.learnkirtan.Constants.Duration.DAY;
import static gsingh.learnkirtan.Constants.Duration.MONTH;
import static gsingh.learnkirtan.Constants.Duration.WEEK;
import gsingh.learnkirtan.Constants.Duration;
import gsingh.learnkirtan.utility.FileUtility;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SettingsManager {

	private static final Logger LOGGER = Logger.getLogger(SettingsManager.class
			.getName());

	/**
	 * The key corresponding to the user's Sa
	 */
	private int saKey;

	private boolean remind;
	private long until;
	private boolean showSargam;
	private boolean showKeys;

	/**
	 * Settings XML document
	 */
	Document dom;

	public SettingsManager() {
		// Retrieve DOM from XML file
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		dom = null;
		try {
			db = dbf.newDocumentBuilder();
			dom = db.parse("config");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get saKey
		saKey = Integer.valueOf(getSetting("sakey"));
		LOGGER.info("Retrieved saKey: " + saKey);

		// Get remind and until values
		until = Long.valueOf(getSetting("until"));
		if (getSetting("remind").equals("yes")) {
			remind = true;
		} else {
			remind = false;
			LOGGER.info("until: " + until);
		}
		LOGGER.info("remind: " + remind);

		// Get label values
		if (getSetting("showsargam").equals("yes")) {
			showSargam = true;
		} else {
			showSargam = false;
		}
		LOGGER.info("showSargam: " + showSargam);

		if (getSetting("showkeys").equals("yes")) {
			showKeys = true;
		} else {
			showKeys = false;
		}
		LOGGER.info("showKeys: " + showKeys);
	}

	private String getSetting(String name) {
		return dom.getElementsByTagName(name).item(0).getTextContent();
	}

	/**
	 * Changes the setting {@code name} in the XML file to {@code value}
	 * 
	 * @param name
	 *            - full name of settings, with '.'s to delimit nodes
	 * @param value
	 *            - value to change the setting to
	 */
	private void changeSetting(String name, String value) {
		LOGGER.info("Setting " + name + " set to " + value);
		dom.getElementsByTagName(name).item(0).setTextContent(value);
		FileUtility.saveSettings(dom);
	}

	public void setShowSargamLabels(boolean bool) {
		String value;
		if (bool)
			value = "yes";
		else
			value = "no";

		showSargam = bool;
		changeSetting("showsargam", value);
	}

	public void setShowKeyboardLabels(boolean bool) {
		String value;
		if (bool)
			value = "yes";
		else
			value = "no";

		showKeys = bool;
		changeSetting("showkeys", value);
	}

	public boolean getShowSargamLabels() {
		return showSargam;
	}

	public boolean getShowKeyboardLabels() {
		return showKeys;
	}

	/**
	 * Turns the update reminder off if {@code bool} is false.
	 * 
	 * @param bool
	 *            - whether to turn update reminders off or not
	 * @param duration
	 *            - used to specify length of time not to remind. 0 is 1 day, 1
	 *            is 1 week, and 2 is 1 month
	 */
	public void setRemind(boolean bool, Duration duration) {
		if (!bool) {
			String d = calculateDate(duration);
			remind = false;
			until = Long.valueOf(d);
			changeSetting("remind", "no");
			changeSetting("until", d);
		} else {
			remind = true;
			changeSetting("remind", "yes");
		}
	}

	/**
	 * Calculates the date (in milliseconds) until reminders will be turned back
	 * on
	 * 
	 */
	private String calculateDate(Duration duration) {
		long time = System.currentTimeMillis();
		long dur = 0;

		if (duration == DAY) {
			dur = 86400000;
		} else if (duration == WEEK) {
			dur = 7 * 86400000;
		} else if (duration == MONTH) {
			dur = 24 * 86400000;
		}

		return String.valueOf(time + dur);
	}

	/**
	 * @return - true if reminders are on, false otherwise
	 */
	public boolean getRemind() {
		return remind;
	}

	/**
	 * Checks whether the duration for not checking reminding about updates is
	 * completed
	 */
	public void checkReminderOffDurationReached() {
		long time = System.currentTimeMillis();

		if (until < time) {
			LOGGER.info("Reminder duration reached.");
			changeSetting("remind", "yes");
			remind = true;
		}
	}

	/**
	 * Sets {@code saKey} to whatever key number the user input minus 1, as the
	 * user counts keys starting from 1
	 * 
	 * @param key
	 *            - the key number input by the user from 1 to 36
	 */
	public void setSaKey(int key) {
		saKey = key - 1;
		changeSetting("sakey", String.valueOf(saKey));
	}

	/**
	 * Returns {@code saKey}
	 */
	public int getSaKey() {
		return saKey;
	}
}

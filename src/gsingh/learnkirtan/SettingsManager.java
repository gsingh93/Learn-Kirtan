package gsingh.learnkirtan;

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
		saKey = Integer.valueOf(dom.getElementsByTagName("sakey").item(0)
				.getTextContent());
		LOGGER.info("Retrieved saKey: " + saKey);
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

	/**
	 * Turns the update reminder off if {@code bool} is false.
	 * 
	 * @param bool
	 *            - whether to turn update reminders off or not
	 * @param duration
	 *            - used to specify length of time not to remind. 0 is 1 day, 1
	 *            is 1 week, and 2 is 1 month
	 */
	public void setRemind(boolean bool, int duration) {
		if (!bool) {
			changeSetting("remind", "no");
			changeSetting("until", calculateDate(duration));
		} else {
			changeSetting("remind", "yes");
		}
	}

	/**
	 * Calculates the date until reminders will be turned back on
	 * 
	 */
	private String calculateDate(int duration) {
		long time = System.currentTimeMillis();
		long dur = 0;

		if (duration == 0) {
			dur = 86400000;
		} else if (duration == 1) {
			dur = 7 * 86400000;
		} else if (duration == 2) {
			dur = 24 * 86400000;
		}

		return String.valueOf(time + dur);
	}

	/**
	 * @return - true if reminders are on, false otherwise
	 */
	public boolean getRemind() {
		if (dom.getElementsByTagName("remind").item(0).getTextContent()
				.equals("yes"))
			return true;
		else
			return false;
	}

	/**
	 * Checks whether the duration for not checking reminding about updates is
	 * completed
	 */
	public void checkReminderOffDurationReached() {
		long until = Long.valueOf(dom.getElementsByTagName("until").item(0)
				.getTextContent());
		long time = System.currentTimeMillis();

		if (until < time) {
			LOGGER.info("Reminder duration reached.");
			changeSetting("remind", "yes");
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

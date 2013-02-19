package gsingh.learnkirtan.settings;

import static gsingh.learnkirtan.Constants.Duration.DAY;
import static gsingh.learnkirtan.Constants.Duration.MONTH;
import static gsingh.learnkirtan.Constants.Duration.WEEK;
import gsingh.learnkirtan.Constants.Duration;
import gsingh.learnkirtan.FileManager;
import gsingh.learnkirtan.listener.SettingsChangedListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * A class for managing persistent settings
 * 
 * @author Gulshan
 * 
 */
public class SettingsManager {

	private static final XPath xPath = XPathFactory.newInstance().newXPath();

	private List<SettingsChangedListener> listeners = new LinkedList<SettingsChangedListener>();

	// TODO: Update the settings names here and in the config file
	/** XPath statement to access the Sa key setting */
	public static final String SA_KEY_XPATH = "/settings/sakey[1]";

	/** XPath statement to access the check for update interval setting */
	private static final String CHECK_FOR_UPDATE_INTERVAL_XPATH = "/settings/updatereminder/until[1]";

	/** XPath statement to access the check for update setting */
	private static final String CHECK_FOR_UPDATE_XPATH = "/settings/updatereminder/remind[1]";

	/** XPath statement to access the show keyboard labels setting */
	public static final String SHOW_KEYBOARD_LABELS_XPATH = "/settings/labels/showkeys[1]";

	/** XPath statement to access the show sargam labels setting */
	public static final String SHOW_SARGAM_LABELS_XPATH = "/settings/labels/showsargam[1]";

	/** The number of milliseconds in a day */
	private static final int DAY_MILLISECONDS = 24 * 60 * 60 * 1000;

	/** The file from which to retrieve and store settings */
	private static final String CONFIG_FILE = "config";

	/** The singleton instance of this class */
	private static SettingsManager instance;

	/** The key corresponding to the user's Sa */
	private int saKey;

	/** Whether or not to check for an update */
	private boolean checkForUpdate;

	/** The interval until the next update check */
	private long checkForUpdateInterval;

	/** Whether to show the sargam labels */
	private boolean showSargamLabels;

	/** Whether to show the keyboard labels */
	private boolean showKeyboardLabels;

	/** The settings XML document */
	private Document dom = null;

	/** The file manager for this class */
	private FileManager fileManager;

	private SettingsManager() {
		// Retrieve DOM from XML file
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;

		try {
			File configFile = new File(CONFIG_FILE);

			if (!configFile.exists()) {
				configFile.createNewFile();
				FileWriter writer = new FileWriter(configFile);
				writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><settings>"
						+ "  <sakey>10</sakey>"
						+ "  <updatereminder>"
						+ "	   <remind>yes</remind>"
						+ "    <until>1</until>"
						+ "	 </updatereminder>"
						+ "	 <labels>"
						+ "	   <showsargam>yes</showsargam>"
						+ "	   <showkeys>yes</showkeys>"
						+ "	 </labels>"
						+ "</settings>");
				writer.close();
			}
			db = dbf.newDocumentBuilder();
			dom = db.parse(CONFIG_FILE);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			initSettings();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	public static SettingsManager getInstance() {
		if (instance == null)
			instance = new SettingsManager();
		return instance;
	}

	public void addSettingsChangedListener(SettingsChangedListener l) {
		listeners.add(l);
	}

	// TODO: Look into a better way to get XML settings
	/**
	 * Initializes the settings variables by reading the config file
	 * 
	 */
	private void initSettings() throws XPathExpressionException {

		// Get saKey
		saKey = Integer.valueOf((String) xPath.evaluate(SA_KEY_XPATH, dom,
				XPathConstants.STRING));

		// Get checkForUpdate settings
		checkForUpdateInterval = Long.valueOf((String)xPath.evaluate(
				CHECK_FOR_UPDATE_INTERVAL_XPATH, dom, XPathConstants.STRING));

		checkForUpdate = (Boolean) xPath.evaluate(CHECK_FOR_UPDATE_XPATH, dom,
				XPathConstants.BOOLEAN);

		// Get label values
		showSargamLabels = (Boolean) xPath.evaluate(SHOW_SARGAM_LABELS_XPATH,
				dom, XPathConstants.BOOLEAN);
		showKeyboardLabels = (Boolean) xPath.evaluate(
				SHOW_KEYBOARD_LABELS_XPATH, dom, XPathConstants.BOOLEAN);
	}

	/** Sets the FileManager for this class */
	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	/**
	 * Sets the show sargam labels setting
	 * 
	 * @param bool
	 *            whether to enable or disable this setting
	 */
	public void setShowSargamLabels(boolean bool) {
		showSargamLabels = bool;
		changeSetting(SHOW_SARGAM_LABELS_XPATH, Boolean.toString(bool));
	}

	/**
	 * Sets the show keyboard labels setting
	 * 
	 * @param bool
	 *            whether to enable or disable this setting
	 */
	public void setShowKeyboardLabels(boolean bool) {
		showKeyboardLabels = bool;
		changeSetting(SHOW_KEYBOARD_LABELS_XPATH, Boolean.toString(bool));
	}

	/** @return the value of the show sargam labels settings */
	public boolean getShowSargamLabels() {
		return showSargamLabels;
	}

	/** @return the value of the show keyboard labels setting */
	public boolean getShowKeyboardLabels() {
		return showKeyboardLabels;
	}

	/**
	 * Sets the value of the checkForUpdate setting
	 * 
	 * @param bool
	 *            whether to turn update checks
	 * @param duration
	 *            used to specify length of time not to remind.
	 */
	public void setCheckForUpdate(boolean bool, Duration duration) {
		if (!bool) {
			String d = calculateDate(duration);
			checkForUpdate = false;
			checkForUpdateInterval = Long.valueOf(d);
			changeSetting(CHECK_FOR_UPDATE_XPATH, Boolean.toString(false));
			changeSetting(CHECK_FOR_UPDATE_INTERVAL_XPATH, d);
		} else {
			checkForUpdate = true;
			changeSetting(CHECK_FOR_UPDATE_XPATH, Boolean.toString(true));
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
			dur = DAY_MILLISECONDS;
		} else if (duration == WEEK) {
			dur = 7 * DAY_MILLISECONDS;
		} else if (duration == MONTH) {
			dur = 24 * DAY_MILLISECONDS;
		}

		return String.valueOf(time + dur);
	}

	/**
	 * @return the value of the reminder setting
	 */
	public boolean getCheckForUpdate() {
		return checkForUpdate;
	}

	/**
	 * Checks whether the duration for not checking reminding about updates is
	 * completed
	 * 
	 */
	public void checkReminderOffDurationReached() {
		long time = System.currentTimeMillis();

		if (checkForUpdateInterval < time) {
			changeSetting(CHECK_FOR_UPDATE_XPATH, Boolean.toString(true));
			checkForUpdate = true;
		}
	}

	/**
	 * Sets {@code saKey} to {@code key}
	 * 
	 * @param key
	 *            the key number input by the user from 1 to 36
	 */
	public void setSaKey(int key) {
		saKey = key;
		changeSetting(SA_KEY_XPATH, String.valueOf(saKey));
	}

	/**
	 * Returns the key id of the Sa key
	 */
	public int getSaKey() {
		return saKey;
	}

	/**
	 * Changes a setting in the config file
	 * 
	 * @param name
	 *            full name of the setting, with '.'s to delimit nodes
	 * @param newValue
	 *            value to change the setting to
	 */
	private void changeSetting(String xPathQuery, String newValue) {
		try {
			Node node = (Node) xPath.evaluate(xPathQuery, dom,
					XPathConstants.NODE);
			String oldValue = node.getTextContent();
			node.setTextContent(newValue);
			// TODO Handle error
			fileManager.saveSettings(dom);

			for (SettingsChangedListener l : listeners) {
				l.onSettingsChanged(xPathQuery, oldValue, newValue);
			}
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
}

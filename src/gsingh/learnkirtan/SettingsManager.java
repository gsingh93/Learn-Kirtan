package gsingh.learnkirtan;

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

	// TODO: Read settings from file
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

	public void changeSetting(String name, String value) {
		dom.getElementsByTagName(name).item(0).setTextContent(value);
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
		FileUtility.saveSettings(dom);
	}

	/**
	 * Returns {@code saKey}
	 */
	public int getSaKey() {
		return saKey;
	}
}

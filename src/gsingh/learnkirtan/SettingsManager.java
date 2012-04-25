package gsingh.learnkirtan;

public class SettingsManager {
	/**
	 * The key corresponding to the user's Sa
	 */
	private int saKey;

	// TODO: Read settings from file
	public SettingsManager(int saKey) {
		this.saKey = saKey;
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
	}

	/**
	 * Returns {@code saKey}
	 */
	public int getSaKey() {
		return saKey;
	}
}

package gsingh.learnkirtan.ui;

import static gsingh.learnkirtan.Constants.BLACK_KEY_WIDTH;
import static gsingh.learnkirtan.Constants.MAX_KEYS;
import static gsingh.learnkirtan.Constants.WHITE_KEY_WIDTH;
import gsingh.learnkirtan.keys.BlackKey;
import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.KeyMapper;
import gsingh.learnkirtan.keys.LabelManager;
import gsingh.learnkirtan.keys.WhiteKey;
import gsingh.learnkirtan.listener.SettingsChangedListener;
import gsingh.learnkirtan.settings.SettingsManager;

import javax.swing.JLayeredPane;

@SuppressWarnings("serial")
public class PianoPanel extends JLayeredPane implements SettingsChangedListener {

	private Key keys[] = new Key[MAX_KEYS];
	private int index;

	private LabelManager labelManager;
	private SettingsManager settingsManager = SettingsManager.getInstance();

	public PianoPanel(LabelManager labelManager) {
		this.labelManager = labelManager;
		int i = 0;
		int j = 0;

		for (int k = 0; k < 3; k++) {
			addWhiteKey(i++);
			addBlackKey(j++);
			addWhiteKey(i++);
			addBlackKey(j++);
			addWhiteKey(i++);
			addWhiteKey(i++);
			j++;
			addBlackKey(j++);
			addWhiteKey(i++);
			addBlackKey(j++);
			addWhiteKey(i++);
			addBlackKey(j++);
			j++;
			addWhiteKey(i++);
		}

		KeyMapper.getInstance().setKeys(keys);

		if (settingsManager.getShowSargamLabels())
			labelManager.labelSargamNotes();
		if (settingsManager.getShowKeyboardLabels())
			labelManager.labelKeyboardNotes();
	}

	/**
	 * Adds a white key to the piano panel
	 * 
	 * @param panel
	 *            - the panel to which to add the key
	 * @param i
	 *            - a number which is used to calculate the position of the key
	 */
	private void addWhiteKey(int i) {
		if (index < MAX_KEYS) {
			WhiteKey b = new WhiteKey(labelManager);
			b.setLocation(i++ * WHITE_KEY_WIDTH, 0);
			add(b, 0, -1);
			keys[index++] = b;
		}
	}

	/**
	 * Adds a black key to the piano panel
	 * 
	 * @param panel
	 *            - the panel to which to add the key
	 * @param factor
	 *            - a number which is used to calculate the position of the key
	 */
	private void addBlackKey(int factor) {
		if (index < MAX_KEYS) {
			BlackKey b = new BlackKey(labelManager);
			b.setLocation(WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 + factor
					* WHITE_KEY_WIDTH, 0);
			add(b, 1, -1);
			keys[index++] = b;
		}
	}

	@Override
	public void onSettingsChanged(String settingsPath, String oldValue,
			String newValue) {
		if (settingsPath.equals(SettingsManager.SA_KEY_XPATH)) {
			if (settingsManager.getShowSargamLabels())
				labelManager.labelSargamNotes();
			if (settingsManager.getShowKeyboardLabels())
				labelManager.labelKeyboardNotes();
		} else if (settingsPath
				.equals(SettingsManager.SHOW_KEYBOARD_LABELS_XPATH)) {
			if (newValue.equals("true"))
				labelManager.labelKeyboardNotes();
			else if (newValue.equals("false"))
				labelManager.clearKeyboardNotes();
			else
				assert false; // TODO Write proper abort code
		} else if (settingsPath
				.equals(SettingsManager.SHOW_SARGAM_LABELS_XPATH)) {
			if (newValue.equals("true"))
				labelManager.labelSargamNotes();
			else if (newValue.equals("false"))
				labelManager.clearSargamNotes();
			else
				assert false;
		}
	}
}

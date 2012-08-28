package gsingh.learnkirtan.keys;

import java.awt.Font;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A class for managing the labels on the keys. This includes the keyboard key
 * mappings and the sargam note mappings
 * 
 * @author Gulshan
 * 
 */
public class LabelManager {

	/** The regex representing a valid keyboard key */
	private static final String KEYBOARD_NOTE_REGEX = "[A-Z;'\\]]*";

	/** The regex representing a valid sargam note */
	private static final String SARGAM_NOTE_REGEX = "[A-Za-z'.]*";

	/** The HTML span tags for keyboard keys */
	private static final String KEYBOARD_SPAN_TAG = "<span id='key'>%s</span>";

	/** The HTML span tags for sargam notes */
	private static final String SARGAM_SPAN_TAG = "<span id='sargam'>%s</span>";

	/** Empty HTML span tags for keyboard keys */
	public static final String EMPTY_KEY_SPAN_TAG = String.format(
			KEYBOARD_SPAN_TAG, "");

	/** Empty HTML span tags for sargam notes */
	public static final String EMPTY_SARGAM_SPAN_TAG = String.format(
			SARGAM_SPAN_TAG, "");

	/** For specifying the octave playable with the keyboard */
	public static enum Octave {
		LOWER, MIDDLE, UPPER
	}

	/** The small font to use for the Dha note */
	private static final Font SMALL_DHA_FONT = new Font("Dialog", Font.PLAIN, 7);

	/** The regular sized font to use for the Dha note */
	private static final Font REGULAR_DHA_FONT = new Font("Dialog", Font.PLAIN,
			9);

	/** The sargam notes */
	private NoteList notes;

	/** The keyboard mapping letters */
	private List<String> keys = new LinkedList<String>(
			Arrays.asList(new String[] { "A", "W", "S", "E", "D", "F", "T",
					"G", "Y", "H", "J", "I", "K", "O", "L", "P", ";", "'", "]" }));

	/** A map of key IDs to keyboard key names for the lower octave */
	private HashMap<Integer, String> keyMapLower = new HashMap<Integer, String>();

	/** A map of key IDs to keyboard key names for the middle octave */
	private HashMap<Integer, String> keyMapMiddle = new HashMap<Integer, String>();

	/** A map of key IDs to keyboard key names for the upper octave */
	private HashMap<Integer, String> keyMapUpper = new HashMap<Integer, String>();

	/** Maps keys to notes */
	private KeyMapper km = KeyMapper.getInstance();

	/** The current octave playable by the keyboard */
	private Octave octave = Octave.MIDDLE;

	public LabelManager(NoteList notes) {
		this.notes = notes;

		// The following code populates the Maps that map a key ID to a keyboard
		// key name. The actual numbers do not need to be understood
		for (int i = 0; i < 13; i++) {
			keyMapLower.put(40 + i, keys.get(i + 5));
		}
		for (int i = 0; i < 19; i++) {
			keyMapMiddle.put(47 + i, keys.get(i));
		}
		for (int i = 0; i < 15; i++) {
			keyMapUpper.put(59 + i, keys.get(i));
		}
	}

	/** @return the octave */
	public Octave getOctave() {
		return octave;
	}

	/**
	 * Shifts the octave down by one octave if possible
	 */
	public void shiftOctaveDown() {
		if (octave == Octave.UPPER)
			octave = Octave.MIDDLE;
		else if (octave == Octave.MIDDLE)
			octave = Octave.LOWER;
	}

	/**
	 * Shifts the octave up by one octave if possible
	 */
	public void shiftOctaveUp() {
		if (octave == Octave.MIDDLE)
			octave = Octave.UPPER;
		else if (octave == Octave.LOWER)
			octave = Octave.MIDDLE;
	}

	/**
	 * Labels all of the sargam notes
	 */
	public void labelSargamNotes() {
		for (Key key : km.getKeys()) {
			labelSargamNote(key);
		}
	}

	/**
	 * Labels all of the keyboard mappings
	 */
	public void labelKeyboardNotes() {
		for (Key key : km.getKeys()) {
			labelKeyboardNote(key);
		}
	}

	/**
	 * Removes all sargam labels
	 */
	public void clearSargamNotes() {
		for (Key key : km.getKeys()) {
			clearSargamNote(key);
		}
	}

	/**
	 * Removes all keyboard mapping labels
	 */
	public void clearKeyboardNotes() {
		for (Key key : km.getKeys()) {
			clearKeyboardNote(key);
		}
	}

	/**
	 * Labels a key with it's sargam note
	 * 
	 * @param key
	 *            the key to label
	 */
	private void labelSargamNote(Key key) {
		clearSargamNote(key);
		if (key instanceof BlackKey) {
			setDhaFont(key);
		}
		key.replaceText(
				EMPTY_SARGAM_SPAN_TAG,
				String.format(SARGAM_SPAN_TAG,
						notes.getNoteName(key.getMIDINoteId() - 40)));
	}

	private void setDhaFont(Key key) {
		if (key.contains("Dha")) {
			key.setFont(SMALL_DHA_FONT);
		} else {
			key.setFont(REGULAR_DHA_FONT);
		}
	}

	/**
	 * Labels a key with it's keyboard mapping
	 * 
	 * @param key
	 *            the key to label
	 */
	private void labelKeyboardNote(Key key) {
		clearKeyboardNote(key);
		if (octave == Octave.LOWER) {
			labelKeyboardNote(key, keyMapLower);
		} else if (octave == Octave.MIDDLE) {
			labelKeyboardNote(key, keyMapMiddle);
		} else if (octave == Octave.UPPER) {
			labelKeyboardNote(key, keyMapUpper);
		}
	}

	/**
	 * Labels a key with a keyboard mapping if found in the provided map
	 * 
	 * @param key
	 *            the key to label
	 * @param keyMap
	 *            the map from which to get the keyboard mapping
	 */
	private void labelKeyboardNote(Key key, Map<Integer, String> keyMap) {
		if (keyMap.containsKey(key.getMIDINoteId()))
			key.replaceText(
					EMPTY_KEY_SPAN_TAG,
					String.format(KEYBOARD_SPAN_TAG,
							keyMap.get(key.getMIDINoteId())));
	}

	/**
	 * Removes a sargam label from a key
	 * 
	 * @param key
	 *            the key to remove the label from
	 */
	private void clearSargamNote(Key key) {
		key.replaceAll(String.format(SARGAM_SPAN_TAG, SARGAM_NOTE_REGEX),
				EMPTY_SARGAM_SPAN_TAG);
	}

	/**
	 * Removes a keyboard mapping label from a key
	 * 
	 * @param key
	 *            the key to remove the label from
	 */
	private void clearKeyboardNote(Key key) {
		key.replaceAll(String.format(KEYBOARD_SPAN_TAG, KEYBOARD_NOTE_REGEX),
				EMPTY_KEY_SPAN_TAG);
	}
}

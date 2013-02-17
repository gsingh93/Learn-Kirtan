package gsingh.learnkirtan.utility;

import gsingh.learnkirtan.keys.LabelManager.Octave;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that contains utility methods that don't belong to other classes
 * 
 * @author Gulshan
 * 
 */
public class Utility {

	private static Map<String, Integer> letterMap = new HashMap<String, Integer>();

	private static final String[] letters = new String[] { "A", "W", "S", "E",
			"D", "F", "T", "G", "Y", "H", "J", "I", "K", "O", "L", "P", ";",
			"\'", "]" };

	static {
		for (int i = 0; i < 18; i++) {
			letterMap.put(letters[i], i + 7);
		}
	}

	/**
	 * Converts the letter from a keyboard mapping to key ID
	 * 
	 * @param letter
	 * @return the key ID the letter corresponds to
	 */
	public static int letterToKeyId(String letter, Octave octave) {
		letter = letter.toUpperCase();

		Integer key = letterMap.get(letter);
		if (key == null) {
			throw new IllegalArgumentException("Letter is invalid");
		}

		if (octave == Octave.LOWER)
			key -= 12;
		else if (octave == Octave.UPPER)
			key += 12;
		return key;
	}
}

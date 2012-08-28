package gsingh.learnkirtan.keys;

import gsingh.learnkirtan.Constants;
import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.parser.Parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NoteList {

	/** The default key ID for sa */
	private static final int DEFAULT_SA_KEY_ID = 10;

	/** The sargam note names for all the keys on the keyboard */
	private List<String> notes = new LinkedList<String>(
			Arrays.asList(new String[] { "Re", "'Ga", "Ga", "Ma", "Ma'", "Pa",
					"'Dha", "Dha", "'Ni", "Ni", "Sa", "'Re", "Re", "'Ga", "Ga",
					"Ma", "Ma'", "Pa", "'Dha", "Dha", "'Ni", "Ni", "Sa", "'Re",
					"Re", "'Ga", "Ga", "Ma", "Ma'", "Pa", "'Dha", "Dha", "'Ni",
					"Ni", "Sa", "'Re" }));

	/** The key ID where the lower octave starts */
	private int lowerStart;

	/** The key ID where the middle octave starts */
	private int middleStart = DEFAULT_SA_KEY_ID;

	/** The key ID where the upper octave starts */
	private int upperStart;

	public NoteList(int saKey) {
		shiftLabels(saKey);
	}

	public void shiftLabels(int saKey) {
		int difference = saKey - middleStart;

		// Shift the sargam labels up or down
		if (difference > 0) {
			for (int i = 0; i < difference; i++) {
				notes.add(0, notes.remove((notes.size() - 1)));
			}
		} else if (difference < 0) {
			for (int i = 0; i < -1 * difference; i++) {
				notes.add(notes.size() - 1, notes.remove(0));
			}
		}

		// Set the middle Sa position
		middleStart = saKey;

		// Set the upper Sa position
		upperStart = saKey + 12;
		if (upperStart >= Constants.MAX_KEYS) {
			upperStart = Constants.MAX_KEYS - 1;
		}

		// Set the lower Sa position
		lowerStart = saKey - 12;
		if (upperStart < 0) {
			upperStart = 0;
		}
	}

	/**
	 * Gets the note name of the note at the specified key ID
	 * 
	 * @param keyId
	 *            the ID of they to get the note name of
	 * @return the name of the note
	 */
	public String getNoteName(int keyId) {
		return notes.get(keyId);
	}

	/**
	 * Returns a {@link Note} corresponding to a specific key ID
	 * 
	 * @param keyId
	 *            the key ID of the key from which to get a note
	 * @return a {@link Note} corresponding to the key
	 */
	public Note getNoteFromId(int keyId) {
		Parser parser = new Parser();

		String note = getNoteName(keyId);

		if (keyId < middleStart) {
			note = "." + note;
		} else if (keyId > upperStart) {
			note = note + ".";
		}

		return parser.parseNote(note);
	}

	public int getLowerNoteIndex(Note note) {
		String name = getName(note);
		int end = middleStart;
		int index = notes.subList(lowerStart, end).indexOf(name);

		return index + lowerStart;
	}

	public int getMiddleNoteIndex(Note note) {
		String name = getName(note);
		int end = upperStart;
		int index = notes.subList(middleStart, end).indexOf(name);

		return index + middleStart;
	}

	public int getUpperNoteIndex(Note note) {
		String name = getName(note);
		int end = notes.size() - 1;
		int index = notes.subList(upperStart, end).indexOf(name);

		return index + upperStart;
	}

	/**
	 * Returns the name of a note including modifiers but not including octaves
	 * 
	 * @param note
	 *            the note to get the name of
	 * @return the name of the note
	 */
	private String getName(Note note) {
		String name = note.getName();
		if (note.isTheevra())
			name = name + "'";
		else if (note.isKomal())
			name = "'" + name;

		return name;
	}
}

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

	/**
	 * Shifts the sargam labels based on the new Sa key
	 * 
	 * @param saKey
	 *            the new saKey to be used as a reference for the shift
	 */
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
	public String getNoteNameFromId(int keyId) {
		return notes.get(keyId);
	}

	/**
	 * Returns a {@link Note} corresponding to a specific key ID
	 * 
	 * @param keyId
	 *            the key ID of the key from which to get a note
	 * @return a {@link Note} corresponding to the key
	 */
	public Note getNoteFromKeyId(int keyId) {
		Parser parser = new Parser();

		String note = getNoteNameFromId(keyId);

		if (keyId < middleStart) {
			note = "." + note;
		} else if (keyId >= upperStart) {
			note = note + ".";
		}

		return parser.parseNote(note);
	}
	
	/**
	 * Returns the index of the note in the lower note section of the note list,
	 * which is the key ID of the note
	 * 
	 * @param note
	 *            the note to get the index of
	 * @return the index of the note
	 */
	public int getLowerNoteIndex(Note note) {
		return getNoteIndex(note, lowerStart, middleStart);
	}

	/**
	 * Returns the index of the note in the middle note section of the note
	 * list, which is the key ID of the note
	 * 
	 * @param note
	 *            the note to get the index of
	 * @return the index of the note
	 */
	public int getMiddleNoteIndex(Note note) {
		return getNoteIndex(note, middleStart, upperStart);
	}

	/**
	 * Returns the index of the note in the upper note section of the note list,
	 * which is the key ID of the note
	 * 
	 * @param note
	 *            the note to get the index of
	 * @return the index of the note
	 */
	public int getUpperNoteIndex(Note note) {
		return getNoteIndex(note, upperStart, notes.size() - 1);
	}

	/**
	 * Gets the index of the provided note in the note list. The note index is
	 * equivalent to the key ID corresponding to the note.
	 * 
	 * @param note
	 *            the note to look for
	 * @param start
	 *            the starting index to look for the note
	 * @param end
	 *            the ending index to look for the note
	 * @return the index of the note
	 */
	private int getNoteIndex(Note note, int start, int end) {
		String name = getName(note);
		int index = notes.subList(start, end).indexOf(name);

		return index + start;
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

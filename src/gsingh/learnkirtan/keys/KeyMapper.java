package gsingh.learnkirtan.keys;

import gsingh.learnkirtan.Constants;
import gsingh.learnkirtan.note.Note;

import java.util.Arrays;

public class KeyMapper {

	private static KeyMapper instance;

	/** The note names for all the notes on the keyboard */
	private NoteList notes;

	/** An array of all of the keys */
	private Key[] keys;

	private KeyMapper() {
	}

	public static KeyMapper getInstance() {
		if (instance == null)
			instance = new KeyMapper();
		return instance;
	}

	/**
	 * Sets the internal {@link NoteList}
	 * 
	 * @param notes
	 *            the new {@link NoteList}
	 */
	public void setNotes(NoteList notes) {
		this.notes = notes;
	}

	/** @return the current {@link NoteList} */
	public NoteList getNotes() {
		return notes;
	}

	/**
	 * Sets the internal {@link Key} array
	 * 
	 * @param keys
	 *            the array
	 */
	public void setKeys(Key keys[]) {
		this.keys = keys;
	}

	/** @return an immutable {@link Iterable} over the keys */
	public final Iterable<Key> getKeys() {
		return Arrays.asList(keys);
	}

	/** @return the {@link Key} corresponding to {@code keyId}, otherwise null */
	public Key getKey(int keyId) {
		if (keyId < Constants.MAX_KEYS && keyId >= 0) {
			return keys[keyId];
		} else {
			return null;
		}
	}

	/**
	 * Returns a key corresponding to a note
	 * 
	 * @param note
	 *            the note to search for
	 * @return the corresponding key
	 */
	public Key getKeyFromNote(Note note) {
		if (note.isUpperOctave()) {
			return keys[notes.getUpperNoteIndex(note)];
		} else if (note.isLowerOctave()) {
			return keys[notes.getLowerNoteIndex(note)];
		} else {
			return keys[notes.getMiddleNoteIndex(note)];
		}
	}
}

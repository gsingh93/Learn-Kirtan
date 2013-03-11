package gsingh.learnkirtan.keys;

import gsingh.learnkirtan.Constants;
import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.note.NoteList;
import gsingh.learnkirtan.parser.exceptions.NoteOutOfBoundsException;

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

	// TODO: This isn't immutable
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
	 * @throws NoteOutOfBoundsException
	 */
	public Key getKeyFromNote(Note note) throws NoteOutOfBoundsException {
		if (note.isUpperOctave()) {
			int index = notes.getUpperNoteIndex(note);
			if (index >= 0 && index < keys.length) {
				return keys[index];
			} else {
				throw new NoteOutOfBoundsException(note.getNoteText());
			}
		} else if (note.isLowerOctave()) {
			int index = notes.getLowerNoteIndex(note);
			if (index >= 0 && index < keys.length) {
				return keys[index];
			} else {
				throw new NoteOutOfBoundsException(note.getNoteText());
			}
		} else {
			int index = notes.getMiddleNoteIndex(note);
			if (index >= 0 && index < keys.length) {
				return keys[index];
			} else {
				throw new NoteOutOfBoundsException(note.getNoteText());
			}
		}
	}
}

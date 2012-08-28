package gsingh.learnkirtan.keys;

import gsingh.learnkirtan.note.Note;

public class KeyMapper {

	private static KeyMapper instance;

	/**
	 * The note names for all the notes on the keyboard
	 */
	private NoteList notes;

	private Key[] keys;

	private KeyMapper() {
	}

	public static KeyMapper getInstance() {
		if (instance == null)
			instance = new KeyMapper();
		return instance;
	}

	public void setNotes(NoteList notes) {
		this.notes = notes;
	}

	public NoteList getNotes() {
		return notes;
	}

	public void setKeys(Key keys[]) {
		this.keys = keys;
	}

	public Key[] getKeys() {
		return keys;
	}

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

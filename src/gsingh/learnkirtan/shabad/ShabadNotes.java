package gsingh.learnkirtan.shabad;

import gsingh.learnkirtan.note.Note;

import java.util.LinkedList;
import java.util.Map;

public class ShabadNotes {
	private LinkedList<Note> notes = new LinkedList<Note>();
	private Map<String, Integer> labelPos;

	public void addLongNote() {
		Note note = notes.get(notes.size() - 1);
		note.setLength(note.getLength() + 1000);
	}

	public void addNote(Note note) {
		notes.add(note);
	}

	public LinkedList<Note> getNotes() {
		return notes;
	}

	public LinkedList<Note> getNotes(String start, String end) {
		return notes;
	}

	public void addLabel(String label, int pos) {
		labelPos.put(label, pos);
	}
}

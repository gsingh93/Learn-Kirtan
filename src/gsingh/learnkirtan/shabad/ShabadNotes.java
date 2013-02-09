package gsingh.learnkirtan.shabad;

import gsingh.learnkirtan.note.Note;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class ShabadNotes implements Iterable<Note>, Serializable {

	private static final long serialVersionUID = 906123591409453595L;

	private LinkedList<Note> notes = new LinkedList<Note>();
	private Map<String, Integer> labelPos;

	public void addLongNote() {
		Note note = notes.get(notes.size() - 1);
		note.setLength(note.getLength() + 1000);
	}

	public void addNote(Note note) {
		notes.add(note);
	}

	public Note get(int index) {
		return notes.get(index);
	}

	// TODO Return a ShabadNotes object
	public LinkedList<Note> getNotes(String start, String end) {
		return notes;
	}

	public int size() {
		return notes.size();
	}

	public void addLabel(String label, int pos) {
		labelPos.put(label, pos);
	}

	@Override
	public Iterator<Note> iterator() {
		return new Iterator<Note>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				if (index < notes.size()) {
					return true;
				} else {
					return false;
				}
			}
			@Override
			public Note next() {
				return notes.get(index++);
			}
			@Override
			public void remove() {
				// Not implemented
			}
		};
	}
}

package gsingh.learnkirtan.shabad;

import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.parser.Parser;

import java.io.Serializable;
import java.util.LinkedList;

public class Shabad implements Serializable {

	private String shabadText;
	private String words;
	private ShabadNotes notes;

	public Shabad(String shabadText, String words) {
		this.shabadText = shabadText;
		this.words = words;
		notes = new Parser().parse(shabadText);
	}

	public Shabad(String shabadText) {
		this(shabadText, "");
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public LinkedList<Note> getNotes() {
		return notes.getNotes();
	}

	public LinkedList<Note> getNotes(String start, String end) {
		return notes.getNotes(start, end);
	}

	public String getShabadText() {
		return shabadText;
	}

	@Override
	public String toString() {
		return String.format("ShabadObject[%s]", getShabadText());
	}
}

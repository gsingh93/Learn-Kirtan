package gsingh.learnkirtan.shabad;

import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.parser.Parser;

import java.io.Serializable;
import java.util.LinkedList;

public class Shabad implements Serializable {

	private static final long serialVersionUID = 6657996411815529195L;

	private String shabadText;
	private String words;
	private ShabadNotes notes;
	private ShabadMetaData metaData;

	public Shabad(String shabadText, String words) {
		this.shabadText = shabadText;
		this.words = words;
		metaData = new ShabadMetaData("", "", "", "", "");
		notes = new Parser().parse(shabadText);
	}

	public Shabad(String shabadText) {
		this(shabadText, "");
	}

	public ShabadMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(ShabadMetaData metaData) {
		this.metaData = metaData;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public ShabadNotes getNotes() {
		return notes;
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

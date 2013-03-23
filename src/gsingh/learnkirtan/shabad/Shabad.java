package gsingh.learnkirtan.shabad;

import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.parser.Parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Shabad implements Serializable {

	private static final long serialVersionUID = 6657996411815529195L;

	private String shabadText;
	private List<String> words;
	private ShabadNotes notes;
	private ShabadMetaData metaData;

	public Shabad(String shabadText, List<String> words) {
		this.shabadText = shabadText;
		this.words = words;
		metaData = new ShabadMetaData("", "", "", "", "");
		notes = new Parser().parse(shabadText);
	}

	public Shabad(String shabadText) {
		this(shabadText, new ArrayList<String>());
	}

	public ShabadMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(ShabadMetaData metaData) {
		this.metaData = metaData;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
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

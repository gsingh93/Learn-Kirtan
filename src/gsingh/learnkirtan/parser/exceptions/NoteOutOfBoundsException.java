package gsingh.learnkirtan.parser.exceptions;


@SuppressWarnings("serial")
public class NoteOutOfBoundsException extends Exception {
	private String note;

	public NoteOutOfBoundsException(String invalidNote) {
		note = invalidNote;
	}

	public String getNote() {
		return note;
	}
}

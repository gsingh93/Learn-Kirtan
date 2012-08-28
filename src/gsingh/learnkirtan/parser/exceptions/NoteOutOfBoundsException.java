package gsingh.learnkirtan.parser.exceptions;

import gsingh.learnkirtan.utility.DialogUtility;

@SuppressWarnings("serial")
public class NoteOutOfBoundsException extends Exception {
	public NoteOutOfBoundsException(String invalidNote) {
		DialogUtility.showNoteOutOfBoundsDialog(invalidNote);

	}
}

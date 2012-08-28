package gsingh.learnkirtan.parser.exceptions;

import gsingh.learnkirtan.utility.DialogUtility;

@SuppressWarnings("serial")
public class InvalidNoteException extends Exception {
	public InvalidNoteException(String invalidNote) {
		DialogUtility.showInvalidNoteDialog(invalidNote);
	}
}

package gsingh.learnkirtan.parser.exceptions;

import gsingh.learnkirtan.utility.DialogUtility;

@SuppressWarnings("serial")
public class DashFirstNoteException extends Exception {
	public DashFirstNoteException() {
		DialogUtility.showDashFirstNoteDialog();
	}
}

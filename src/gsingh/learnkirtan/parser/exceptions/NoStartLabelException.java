package gsingh.learnkirtan.parser.exceptions;

import gsingh.learnkirtan.utility.DialogUtility;

@SuppressWarnings("serial")
public class NoStartLabelException extends Exception {

	public NoStartLabelException() {
		DialogUtility.showNoStartLabelDialog();
	}
}

package gsingh.learnkirtan.parser.exceptions;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class DashFirstNoteException extends Exception {
	public DashFirstNoteException(Logger LOGGER) {
		LOGGER.warning("ERROR: Dash found as first note.");
		JOptionPane
				.showMessageDialog(
						null,
						"Error: The first note found was a dash, which is not allowed.",
						"Error", JOptionPane.ERROR_MESSAGE);
	}
}

package gsingh.learnkirtan.exceptions;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class InvalidNoteException extends Exception {
	public InvalidNoteException(Logger LOGGER, String invalidNote) {
		LOGGER.warning("Invalid note: " + invalidNote);
		JOptionPane.showMessageDialog(null, "Error: Invalid note '"
				+ invalidNote + "'", "Error", JOptionPane.ERROR_MESSAGE);
	}
}

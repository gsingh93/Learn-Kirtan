package gsingh.learnkirtan.exceptions;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class NoteOutOfBoundsException extends Exception {
	public NoteOutOfBoundsException(Logger LOGGER, String invalidNote) {
		LOGGER.warning("Invalid note: " + invalidNote);
		JOptionPane.showMessageDialog(null, "Error: Invalid note '"
				+ invalidNote + "' is too low or too high", "Error",
				JOptionPane.ERROR_MESSAGE);
	}
}

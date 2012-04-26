package gsingh.learnkirtan.exceptions;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class NoStartLabelException extends Exception {

	public NoStartLabelException(Logger LOGGER) {
		LOGGER.warning("No starting label was found. Stopping playback.");
		JOptionPane
				.showMessageDialog(
						null,
						"Error: You specified that playback should start at a label, "
								+ "but that label could not be found. Make sure there is a "
								+ "'#' before the label.", "Error",
						JOptionPane.ERROR_MESSAGE);
	}
}

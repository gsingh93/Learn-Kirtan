package gsingh.learnkirtan.utility;

import static gsingh.learnkirtan.Constants.VERSION;
import gsingh.learnkirtan.shabad.ShabadMetaData;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Static class that wraps various Dialog box calls and provides interfaces to
 * their result values
 * 
 * @author Gulshan
 * 
 */
public class DialogUtility {

	public static int showSaveDialog() {
		return JOptionPane.showConfirmDialog(null,
				"Would you like to save before proceeding?");
	}

	public static int showAboutDialog() {
		return JOptionPane
				.showConfirmDialog(
						null,
						"This software was written by Gulshan Singh (gulshan@umich.edu) and is free \n"
								+ "and opensource under the Apache License. Please contact me if you would like to contribute.\n"
								+ "\n Version " + VERSION, "About",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
	}

	public static int showUpToDateDialog() {
		return JOptionPane.showConfirmDialog(null,
				"The software is up to date.", "No Updates",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}

	public static int showUpdateAvailableDialog(JPanel panel) {
		return JOptionPane.showOptionDialog(null, panel, "Update Available",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, null, null);
	}

	public static int showOverwriteDialog() {
		return JOptionPane.showConfirmDialog(null, "Overwrite existing file?",
				"Confirm Overwrite", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
	}

	public static void showFileDoesntExistDialog() {
		JOptionPane.showMessageDialog(null, "Error: File doesn't exist.",
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void showInvalidShabadDialog() {
		JOptionPane
				.showMessageDialog(
						null,
						"The shabad contains errors. Please fix all red colored cells.",
						"Error", JOptionPane.ERROR_MESSAGE);
	}

	public static int showChangeSaDialog(JPanel panel) {
		return JOptionPane.showConfirmDialog(null, panel, "Change Sa Key",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	}

	public static void showDashFirstNoteDialog() {
		JOptionPane
				.showMessageDialog(
						null,
						"Error: The first note found was a dash, which is not allowed.",
						"Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void showNoStartLabelDialog() {
		JOptionPane
				.showMessageDialog(
						null,
						"Error: You specified that playback should start at a label, "
								+ "but that label could not be found. Make sure there is a "
								+ "'#' before the label.", "Error",
						JOptionPane.ERROR_MESSAGE);
	}

	public static void showNoteOutOfBoundsDialog(String invalidNote) {
		JOptionPane.showMessageDialog(null, "Error: Invalid note '"
				+ invalidNote + "' is too low or too high", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/*************************************************************************/

	public static boolean isCancelledOrClosed(int result) {
		if (result == JOptionPane.CANCEL_OPTION
				|| result == JOptionPane.CLOSED_OPTION) {
			return true;
		} else
			return false;
	}

	public static boolean isYes(int result) {
		if (result == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	public static boolean isOK(int result) {
		if (result == JOptionPane.OK_OPTION)
			return true;
		else
			return false;
	}

	public static void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	public static void showMetaDataDialog(ShabadMetaData metaData) {
		JTextField name = new JTextField(15);
		JTextField taal = new JTextField(15);
		JTextField raag = new JTextField(15); // TODO Dropdown
		JTextField ang = new JTextField(15); // TODO Spinner
		JTextArea notes = new JTextArea(3, 15);
		notes.setBorder(BorderFactory.createBevelBorder(1));

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(new JLabel("Name: "));
		panel.add(name);
		panel.add(new JLabel("Taal: "));
		panel.add(taal);
		panel.add(new JLabel("Raag: "));
		panel.add(raag);
		panel.add(new JLabel("Ang: "));
		panel.add(ang);
		panel.add(new JLabel("Notes: "));
		panel.add(notes);

		JDialog dialog = new JDialog();
		dialog.setContentPane(panel);
		dialog.setLocation(400, 200); // TODO
		dialog.pack();
		dialog.setVisible(true);
	}
}

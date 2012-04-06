package gsingh.learnkirtan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileIO {

	/**
	 * The logger for this class
	 */
	private final static Logger LOGGER = Logger.getLogger(FileIO.class
			.getName());

	/**
	 * The filechooser used for opening and saving files
	 */
	private static final JFileChooser fc;
	static {
		fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("SBD (Shabad) File",
				"sbd");
		fc.setFileFilter(filter);
	}

	/**
	 * Saves the text in the shabadEditor to the file specified. If no file is
	 * specified, the user is prompted to specify one.
	 * 
	 * @throws IOException
	 */
	public static int save(JTextArea shabadEditor, File file)
			throws IOException {
		LOGGER.info("Save process started.");
		if (file == null) {
			LOGGER.info("User will be prompted for a save location.");
			int returnVal = fc.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				String filename = file.getName();
				if (filename.length() <= 4) {
					filename = filename + ".sbd";
					file = new File(file.getAbsolutePath() + ".sbd");
					LOGGER.info(".sbd extension was automatically appended.");
				} else if (!filename.substring(filename.length() - 4).equals(
						".sbd")) {
					filename = filename + ".sbd";
					file = new File(file.getAbsolutePath() + ".sbd");
					LOGGER.info(".sbd extension was supplied.");
				}
				LOGGER.info("Filename Chosen: " + filename);
				if (file.exists()) {
					LOGGER.info("File specified already exists.");
					int result = JOptionPane.showConfirmDialog(null,
							"Overwrite existing file?", "Confirm Overwrite",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (result == JOptionPane.OK_OPTION) {
						LOGGER.warning("User chose to overwrite.");
						FileIO.write(shabadEditor, file);
						return 2;
					} else {
						LOGGER.info("User chose not to overwrite.");
					}
				} else {
					LOGGER.info("User specified a new file. Proceeding with save.");
					FileIO.write(shabadEditor, file);
					return 2;
				}
			}
		} else {
			LOGGER.info("User is saving to an already chosen file.");
			FileIO.write(shabadEditor, file);
			return 1;
		}

		return -1;
	}

	/**
	 * Write the shabadEditor text to a file
	 * 
	 * @throws IOException
	 */
	public static void write(JTextArea shabadEditor, File file)
			throws IOException {
		LOGGER.fine("File write started.");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		shabadEditor.write(bw);
		bw.close();

		LOGGER.fine("File write completed.");
	}

	/**
	 * Prompts the user for a file to open and opens the selected file
	 */
	public static File openFile(JTextArea shabadEditor, File file) {
		LOGGER.fine("File open process started.");
		int returnVal = fc.showOpenDialog(null);
		BufferedReader br = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			LOGGER.info("File Chosen: " + file.getName());
			if (file.exists()) {
				try {
					LOGGER.fine("File open started");
					br = new BufferedReader(new FileReader(file));

					// Read file text into editor
					String line;
					shabadEditor.setText("");
					while ((line = br.readLine()) != null) {
						shabadEditor.append(line + '\n');
					}

					br.close();

					LOGGER.fine("File write completed.");
					LOGGER.fine("File open process finished.");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					file = null;
				} catch (IOException e2) {
					e2.printStackTrace();
					file = null;
				}
			} else {
				file = null;
				LOGGER.info("File doesn't exist.");
				JOptionPane.showMessageDialog(null,
						"Error: File doesn't exist.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		LOGGER.fine("File open process finished.");

		System.out.println(file);
		return file;
	}
}

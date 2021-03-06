package gsingh.learnkirtan;

import gsingh.learnkirtan.listener.FileEventListener;
import gsingh.learnkirtan.listener.FileEventListener.FileEvent;
import gsingh.learnkirtan.shabad.Shabad;
import gsingh.learnkirtan.ui.shabadeditor.ShabadEditor;
import gsingh.learnkirtan.utility.DialogUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * Responsible for managing the opening and saving of files
 * 
 * @author Gulshan
 * 
 */
public class FileManager {

	public enum SaveResult {
		NOT_SAVED_CANCELLED, NOT_SAVED, SAVEDEXISTING, SAVED_NEW, OVERWRITE, NOT_SAVED_INVALID_SHABAD
	}

	private List<FileEventListener> listeners = new LinkedList<FileEventListener>();

	private static final int VERSION = 2;

	/** The file extension of Shabad files */
	private static final String EXTENSION = ".sbd";

	/** The path to the settings files */
	private static final String SETTINGS_FILE = "config";

	/** The filechooser used for opening and saving files */
	private final JFileChooser fc;
	{
		fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("SBD (Shabad) File",
				"sbd");
		fc.setFileFilter(filter);
	}

	/**
	 * The file in which your shabad will be saved or was opened from. When the
	 * program is first started, it has the value of {@code null}.
	 */
	private File curFile;

	public void addFileEventListener(FileEventListener l) {
		listeners.add(l);
	}

	/**
	 * Writes a DOM to the settings file
	 * 
	 * @param dom
	 *            The DOM to persist
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public void saveSettings(Document dom)
			throws TransformerFactoryConfigurationError, TransformerException {
		// Prepare the DOM document for writing
		Source source = new DOMSource(dom);

		// Prepare the output file
		File file = new File(SETTINGS_FILE);
		Result result = new StreamResult(file);

		// Write the DOM document to the file
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(source, result);
	}

	/**
	 * Sets the invariants for a new file
	 */
	public void newFile() {
		curFile = null;

		for (FileEventListener l : listeners) {
			FileEvent e = FileEvent.CREATE;
			e.setFileName("");
			l.onFileEvent(e);
		}
	}

	/**
	 * Saves the text in the shabadEditor to the file specified. If no file is
	 * specified, the user is prompted to specify one.
	 * 
	 * @throws IOException
	 */
	public SaveResult saveShabad(Shabad shabad) throws IOException {

		// If no file is currently opened
		if (!isFileOpened()) {
			int returnVal = fc.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				curFile = fc.getSelectedFile();

				curFile = appendFileExtension(curFile);

				// If the selected file exists and we're about to overwrite it
				if (curFile.exists()) {
					int result = DialogUtility.showOverwriteDialog();

					// If the user is OK with overwriting it
					if (DialogUtility.isOK(result)) {
						write(shabad);
						return SaveResult.OVERWRITE;
					} else {
						return SaveResult.NOT_SAVED_CANCELLED;
					}
				} else {
					write(shabad);
					return SaveResult.SAVED_NEW;
				}
			} else {
				return SaveResult.NOT_SAVED_CANCELLED;
			}
		} else {
			write(shabad);
			return SaveResult.SAVEDEXISTING;
		}
	}

	/**
	 * Returns true if a file is currently opened, false otherwise
	 * 
	 */
	public boolean isFileOpened() {
		if (curFile == null)
			return false;
		else
			return true;
	}

	/**
	 * Append the file extension if not present
	 * 
	 * @param file
	 *            The file to append the extension to
	 */
	private File appendFileExtension(File file) {
		String filename = file.getName();
		if (isMissingExtension(filename)) {
			curFile = new File(curFile.getAbsolutePath() + EXTENSION);
		}

		return curFile;
	}

	/**
	 * @param filename
	 *            the filename to check
	 * @return true if filename does not end in the file extension
	 */
	private boolean isMissingExtension(String filename) {
		if (filename.length() <= 4
				|| !filename.substring(filename.length() - 4).equals(EXTENSION))
			return true;
		else
			return false;
	}

	/**
	 * Writes a {@link Shabad} to a file
	 * 
	 * @throws IOException
	 */
	private void write(Shabad shabad) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(curFile);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		try {
			out.writeInt(VERSION);
			out.writeObject(shabad);
		} finally {
			out.close();
			fileOut.close();
		}

		for (FileEventListener l : listeners) {
			FileEvent e = FileEvent.SAVE;
			e.setFileName(curFile.getName());
			l.onFileEvent(e);
		}
	}

	/**
	 * Prompts the user for a file to open and opens the selected file.
	 * 
	 * @return true if a file was opened, false otherwise
	 * 
	 * @throws IOException
	 */
	public boolean openFile(ShabadEditor shabadEditor) throws IOException {
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			if (file.exists()) {
				FileInputStream fileIn = new FileInputStream(file);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				Shabad shabad = null;
				try {
					int version = in.readInt();
					shabad = (Shabad) in.readObject();
				} catch (ClassNotFoundException e) {
					// Programmer error: should never happen
					e.printStackTrace();
				} finally {
					in.close();
					fileIn.close();
				}

				shabadEditor.setShabad(shabad);

				curFile = file;

				for (FileEventListener l : listeners) {
					FileEvent e = FileEvent.OPEN;
					e.setFileName(curFile.getName());
					l.onFileEvent(e);
				}

				return true;
			} else {
				DialogUtility.showFileDoesntExistDialog();
			}
		}

		return false;
	}

	/**
	 * Gets the name of the currently opened file
	 * 
	 * @return The name of the opened file. If no file is opened, returns the
	 *         empty string
	 */
	public String getFileName() {
		if (isFileOpened()) {
			return curFile.getName();
		} else {
			return "";
		}
	}
}

package gsingh.learnkirtan.ui;

import static gsingh.learnkirtan.Main.BASETITLE;
import gsingh.learnkirtan.FileManager;
import gsingh.learnkirtan.listener.FileEventListener;

import javax.swing.JFrame;

/**
 * Responsible for managing the title of the window
 * 
 * @author Gulshan
 * 
 */
public class WindowTitleManager implements FileEventListener {

	private JFrame frame;
	private FileManager fileManager;

	public WindowTitleManager(JFrame frame, FileManager fileManager) {
		this.frame = frame;
		this.fileManager = fileManager;
	}

	/** Sets the title for when an untitled document is open */
	public void setDocumentCreatedTitle() {
		setBaseTitle("Untitled Shabad");
	}

	/** Sets the title for when a file is saved */
	public void setDocumentSavedTitle(String filename) {
		setOpenedTitle(filename);
		// removeAsterisk();
	}

	/** Sets the title for when a file is opened */
	public void setOpenedTitle(String filename) {
		setBaseTitle(filename);
	}

	/** Sets the title for when a file is unmodified */
	public void setDocumentUnmodifiedTitle() {
		removeAsterisk();
	}

	/** Sets the title for when a file is modified */
	public void setDocumentModifiedTitle() {
		setAsterisk();
	}

	private void setBaseTitle(String filename) {
		frame.setTitle(BASETITLE + filename);
	}

	private void removeAsterisk() {
		String title = frame.getTitle();
		if (title.contains("*")) {
			frame.setTitle(title.substring(0, title.length() - 1));
		}
	}

	private void setAsterisk() {
		String title = frame.getTitle();
		if (!title.contains("*"))
			frame.setTitle(title + "*");
	}

	@Override
	public void onFileEvent(FileEvent e) {
		if (e == FileEvent.SAVE) {
			setDocumentSavedTitle(fileManager.getFileName());
		} else if (e == FileEvent.CREATE) {
			setDocumentCreatedTitle();
		} else {
			setOpenedTitle(fileManager.getFileName());
		}
	}
}

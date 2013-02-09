package gsingh.learnkirtan.ui.menu.controller;

import gsingh.learnkirtan.FileManager;
import gsingh.learnkirtan.FileManager.SaveResult;
import gsingh.learnkirtan.shabad.Shabad;
import gsingh.learnkirtan.shabad.ShabadMetaData;
import gsingh.learnkirtan.ui.shabadeditor.ShabadEditor;
import gsingh.learnkirtan.ui.shabadeditor.SwingShabadEditor;
import gsingh.learnkirtan.utility.DialogUtility;

import java.io.IOException;

public class FileMenuController {

	private ShabadEditor shabadEditor;
	private FileManager fileManager;

	public FileMenuController(FileManager fileManager, ShabadEditor shabadEditor) {
		this.fileManager = fileManager;
		this.shabadEditor = shabadEditor;
	}

	public void open() {
		try {
			// Save file if modified
			SaveResult result = null;
			if (shabadEditor.isModified()) {
				result = fileManager.safeSave(shabadEditor.getShabad());
			}

			// Open file if not cancelled
			if (result != SaveResult.NOTSAVEDCANCELLED) {
				if (fileManager.openFile(shabadEditor)) {
					if (shabadEditor instanceof SwingShabadEditor) {
						SwingShabadEditor editor = (SwingShabadEditor) shabadEditor;
						editor.reset();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void save() {
		try {
			if (shabadEditor.isModified()) {
				fileManager.saveShabad(shabadEditor.getShabad());
				if (shabadEditor instanceof SwingShabadEditor) {
					SwingShabadEditor editor = (SwingShabadEditor) shabadEditor;
					editor.reset();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void create() {
		try {
			// Save file if modified
			SaveResult result = null;
			if (shabadEditor.isModified()) {
				result = fileManager.safeSave(shabadEditor.getShabad());
			}

			// Create new file if not cancelled
			if (result != SaveResult.NOTSAVEDCANCELLED) {
				shabadEditor.setShabad(new Shabad(""));
				fileManager.newFile();
				if (shabadEditor instanceof SwingShabadEditor) {
					SwingShabadEditor editor = (SwingShabadEditor) shabadEditor;
					editor.reset();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void properties() {
		DialogUtility.showMetaDataDialog(shabadEditor.getMetaData(),
				new MetaDataDialogCallback());
	}

	public class MetaDataDialogCallback {
		public void completed(ShabadMetaData data) {
			shabadEditor.setMetaData(data);
			// TODO set modified
		}
	}
}
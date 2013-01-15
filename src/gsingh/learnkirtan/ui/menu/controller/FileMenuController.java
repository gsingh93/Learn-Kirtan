package gsingh.learnkirtan.ui.menu.controller;

import gsingh.learnkirtan.FileManager;
import gsingh.learnkirtan.FileManager.SaveResult;
import gsingh.learnkirtan.StateModel;
import gsingh.learnkirtan.StateModel.FileState;
import gsingh.learnkirtan.WindowTitleManager;
import gsingh.learnkirtan.ui.shabadeditor.ShabadEditor;
import gsingh.learnkirtan.ui.shabadeditor.SwingShabadEditor;

import java.io.IOException;

public class FileMenuController {

	private WindowTitleManager titleManager;

	private StateModel model;
	private ShabadEditor shabadEditor;
	private FileManager fileManager;

	public FileMenuController(StateModel model, FileManager fileManager,
			WindowTitleManager titleManager, ShabadEditor shabadEditor) {
		this.model = model;
		this.fileManager = fileManager;
		this.titleManager = titleManager;
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
					titleManager.setOpenedTitle(fileManager.getFileName());
					if (shabadEditor instanceof SwingShabadEditor) {
						SwingShabadEditor editor = (SwingShabadEditor) shabadEditor;
						editor.reset();
					}
					model.setFileState(FileState.OPEN);
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
			}
			titleManager.setDocumentSavedTitle(fileManager.getFileName());
			model.setFileState(FileState.SAVE);
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
				fileManager.newFile();
				shabadEditor.setText("");
				if (shabadEditor instanceof SwingShabadEditor) {
					SwingShabadEditor editor = (SwingShabadEditor) shabadEditor;
					editor.reset();
				}
				titleManager.setDocumentCreatedTitle();

				model.setFileState(FileState.CREATE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
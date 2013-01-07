package gsingh.learnkirtan.controller;

import gsingh.learnkirtan.FileManager;
import gsingh.learnkirtan.StateModel;
import gsingh.learnkirtan.WindowTitleManager;
import gsingh.learnkirtan.component.shabadeditor.SwingShabadEditor;
import gsingh.learnkirtan.controller.menu.FileMenuController;
import gsingh.learnkirtan.controller.menu.HelpMenuController;
import gsingh.learnkirtan.controller.menu.KeyboardMenuController;
import gsingh.learnkirtan.controller.menu.OptionsMenuController;
import gsingh.learnkirtan.keys.LabelManager;
import gsingh.learnkirtan.note.NoteList;

public class ControllerFactory {

	private FileManager fileManager;
	private StateModel model;
	private NoteList notes;
	private SwingShabadEditor shabadEditor;
	private WindowTitleManager titleManager;
	private LabelManager labelManager;

	public ControllerFactory(FileManager fileManager, StateModel model,
			NoteList notes, SwingShabadEditor shabadEditor,
			WindowTitleManager titleManager, LabelManager labelManager) {
		this.fileManager = fileManager;
		this.model = model;
		this.notes = notes;
		this.shabadEditor = shabadEditor;
		this.titleManager = titleManager;
		this.labelManager = labelManager;
	}

	public FileMenuController getFileMenuController() {
		return new FileMenuController(model, fileManager, titleManager,
				shabadEditor);
	}

	public KeyboardMenuController getKeyboardMenuController() {
		return new KeyboardMenuController(model, shabadEditor);
	}

	public OptionsMenuController getOptionsMenuController() {
		return new OptionsMenuController(model, notes, labelManager);
	}

	public HelpMenuController getHelpMenuController() {
		return new HelpMenuController(model);
	}
}

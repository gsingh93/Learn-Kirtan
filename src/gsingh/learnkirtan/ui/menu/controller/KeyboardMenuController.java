package gsingh.learnkirtan.ui.menu.controller;

import gsingh.learnkirtan.StateModel;
import gsingh.learnkirtan.ui.shabadeditor.SwingShabadEditor;

public class KeyboardMenuController {

	// private static enum Mode {
	// EDIT, COMPOSE
	// }

	// TODO: Mode is not used?
	// private Mode mode;
	private SwingShabadEditor shabadEditor;

	public KeyboardMenuController(StateModel model,
			SwingShabadEditor shabadEditor) {
		this.shabadEditor = shabadEditor;
	}

	public void setComposeMode() {
		// mode = Mode.COMPOSE;
		shabadEditor.setEditable(false);
	}

	public void setEditMode() {
		// mode = Mode.EDIT;
		shabadEditor.setEditable(true);
	}

}

package gsingh.learnkirtan.ui.menu.controller;

import gsingh.learnkirtan.ui.shabadeditor.SwingShabadEditor;

public class KeyboardMenuController {

	private SwingShabadEditor shabadEditor;

	public KeyboardMenuController(SwingShabadEditor shabadEditor) {
		this.shabadEditor = shabadEditor;
	}

	public void setComposeMode() {
		shabadEditor.setEditable(false);
	}

	public void setEditMode() {
		shabadEditor.setEditable(true);
	}

}

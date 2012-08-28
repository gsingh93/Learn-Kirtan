package gsingh.learnkirtan.component.menu;

import gsingh.learnkirtan.component.shabadeditor.SwingShabadEditor;
import gsingh.learnkirtan.controller.ControllerFactory;

import javax.swing.JMenuBar;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	public MenuBar(ControllerFactory factory, SwingShabadEditor shabadEditor) {

		add(new FileMenu(factory.getFileMenuController()));
		add(new EditMenu(shabadEditor.getUndoAction(),
				shabadEditor.getRedoAction()));
		add(new KeyboardMenu(factory.getKeyboardMenuController()));
		add(new OptionsMenu(factory.getOptionsMenuController()));
		add(new HelpMenu(factory.getHelpMenuController()));
	}
}

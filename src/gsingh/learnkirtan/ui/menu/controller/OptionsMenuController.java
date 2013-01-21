package gsingh.learnkirtan.ui.menu.controller;

import gsingh.learnkirtan.keys.LabelManager;
import gsingh.learnkirtan.note.NoteList;
import gsingh.learnkirtan.settings.SettingsManager;
import gsingh.learnkirtan.utility.DialogUtility;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class OptionsMenuController {

	private SettingsManager settingsManager = SettingsManager.getInstance();
	private LabelManager labelManager;

	private NoteList notes;

	public OptionsMenuController(NoteList notes,
			LabelManager labelManager) {
		this.notes = notes;
		this.labelManager = labelManager;
	}

	public void changeSa() {
		SpinnerModel saModel = new SpinnerNumberModel(
				settingsManager.getSaKey() + 1, 1, 22, 1);
		JSpinner saSpinner = new JSpinner(saModel);

		JPanel panel = new JPanel();
		panel.add(new JLabel("Choose the key number for sa:"));
		panel.add(saSpinner);

		int result = DialogUtility.showChangeSaDialog(panel);

		if (DialogUtility.isOK(result)) {
			int value = (Integer) saSpinner.getValue() - 1;
			settingsManager.setSaKey(value);
			notes.shiftLabels(value);

			if (settingsManager.getShowSargamLabels())
				labelManager.labelSargamNotes();
			if (settingsManager.getShowKeyboardLabels())
				labelManager.labelKeyboardNotes();
		}
	}

	public void showSargamLabels(boolean bool) {
		if (bool) {
			labelManager.labelSargamNotes();
		} else {
			labelManager.clearSargamNotes();
		}
		settingsManager.setShowSargamLabels(bool);
	}

	public void showKeyboardLabels(boolean bool) {
		if (bool) {
			labelManager.labelKeyboardNotes();
		} else {
			labelManager.clearKeyboardNotes();
		}
		settingsManager.setShowKeyboardLabels(bool);
	}
}

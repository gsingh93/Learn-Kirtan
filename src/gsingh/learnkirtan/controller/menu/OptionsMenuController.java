package gsingh.learnkirtan.controller.menu;

import static gsingh.learnkirtan.Constants.MAX_KEYS;
import gsingh.learnkirtan.StateModel;
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

	// TODO: model is not used?
	// private StateModel model;
	private NoteList notes;

	public OptionsMenuController(StateModel model, NoteList notes,
			LabelManager labelManager) {
		// this.model = model;
		this.notes = notes;
		this.labelManager = labelManager;
	}

	public void changeSa() {
		SpinnerModel saModel = new SpinnerNumberModel(
				settingsManager.getSaKey() + 1, 1, MAX_KEYS, 1);
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

package gsingh.learnkirtan.component;

import gsingh.learnkirtan.StateModel.FileState;
import gsingh.learnkirtan.StateModel.PlayState;
import gsingh.learnkirtan.component.shabadeditor.SwingShabadEditor;
import gsingh.learnkirtan.player.ShabadPlayer;
import gsingh.learnkirtan.utility.DialogUtility;
import gsingh.learnkirtan.validation.ValidationError;
import gsingh.learnkirtan.validation.ValidationErrors;
import gsingh.learnkirtan.validation.Validator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements ActionListener,
		ItemListener, View {

	/**
	 * A spinner controlling tempo. It is set to 1.0 by default, has an
	 * increment of 0.1, and has a range from .1 to 2. The shabad plays at this
	 * multiplier times the default speed (in the implementation, the length of
	 * each keypress is divided by this value to have the same effect). It
	 * cannot be changed while playing.
	 */
	private JSpinner tempoControl;

	/**
	 * If checked, the selected playback lines will repeat when finished
	 */
	private JCheckBox repeat;

	/**
	 * If not empty, the playback will start here
	 */
	private JTextField startField;

	/**
	 * If not empty, the playback will end here
	 */
	private JTextField endField;

	private ShabadPlayer controller;
	private SwingShabadEditor shabadEditor;

	public ControlPanel(ShabadPlayer controller, SwingShabadEditor shabadEditor) {
		this.controller = controller;
		this.shabadEditor = shabadEditor;

		JButton playButton = new JButton("Play");
		JButton pauseButton = new JButton("Pause");
		JButton stopButton = new JButton("Stop");

		playButton.addActionListener(this);
		playButton.setActionCommand("play");

		pauseButton.addActionListener(this);
		pauseButton.setActionCommand("pause");

		stopButton.addActionListener(this);
		stopButton.setActionCommand("stop");

		JLabel tempoLabel = new JLabel("Tempo:");

		SpinnerNumberModel model = new SpinnerNumberModel(1, .1, 2, .1);
		tempoControl = new JSpinner(model);
		JSpinner.NumberEditor editor = (JSpinner.NumberEditor) tempoControl
				.getEditor();
		DecimalFormat format = editor.getFormat();
		format.setMinimumFractionDigits(1);
		Dimension d = tempoControl.getPreferredSize();
		d.width = 40;
		tempoControl.setPreferredSize(d);

		repeat = new JCheckBox("Repeat");
		repeat.addItemListener(this);

		JLabel startLabel = new JLabel("Start Label:");
		JLabel endLabel = new JLabel("End Label:");
		startField = new JTextField(7);
		endField = new JTextField(7);

		add(playButton);
		add(pauseButton);
		add(stopButton);
		add(tempoLabel);
		add(tempoControl);
		add(repeat);
		add(startLabel);
		add(startField);
		add(endLabel);
		add(endField);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("play")) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					ValidationErrors errors = Validator.validate(shabadEditor
							.getText());
					if (errors.noErrors()) {
						controller.play(shabadEditor.getShabad());
					} else {
						// Display the errors
						displayErrors(errors);
					}
				}

			}).start();
		} else if (command.equals("pause")) {
			controller.pause();
		} else if (command.equals("stop")) {
			controller.stop();
		}
	}

	private void displayErrors(ValidationErrors errors) {
		StringBuilder sb = new StringBuilder();
		for (ValidationError error : errors.getErrors()) {
			sb.append(String
					.format("An error occured on line %d at position %d with token %s. Error message: %s\n",
							error.getLine(), error.getPos(), error.getToken(),
							error.getMessage()));
		}

		DialogUtility.showMessage(sb.toString());
	}

	private class MyPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			if (e.getNewValue() == FileState.OPEN) {
				startField.setText("");
				endField.setText("");
				startField.setEnabled(false);
			} else if (e.getNewValue() == PlayState.PLAY) {
				boolean bool = false;
				shabadEditor.setEnabled(bool);
				tempoControl.setEnabled(bool);
				repeat.setEnabled(bool);
				startField.setEnabled(bool);
				endField.setEnabled(bool);
			} else if (e.getNewValue() == PlayState.STOP) {
				boolean bool = true;
				shabadEditor.setEnabled(bool);
				tempoControl.setEnabled(bool);
				repeat.setEnabled(bool);
				startField.setEnabled(bool);
				endField.setEnabled(bool);
			}
		}
	}

	@Override
	public PropertyChangeListener getPropertyChangeListener() {
		return new MyPropertyChangeListener();
	}

}

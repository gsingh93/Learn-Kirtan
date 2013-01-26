package gsingh.learnkirtan.ui;

import gsingh.learnkirtan.listener.FileEventListener;
import gsingh.learnkirtan.listener.PlayEventListener;
import gsingh.learnkirtan.player.ShabadPlayer;
import gsingh.learnkirtan.ui.shabadeditor.SwingShabadEditor;
import gsingh.learnkirtan.ui.shabadeditor.tableeditor.TableShabadEditor;
import gsingh.learnkirtan.utility.DialogUtility;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements ActionListener,
		ItemListener, FileEventListener, PlayEventListener {

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

	JButton playButton = new JButton("Play");
	JButton pauseButton = new JButton("Pause");
	JButton stopButton = new JButton("Stop");

	private ShabadPlayer shabadPlayer;
	private SwingShabadEditor shabadEditor;

	public ControlPanel(ShabadPlayer shabadPlayer,
			final SwingShabadEditor shabadEditor) {
		this.shabadPlayer = shabadPlayer;
		this.shabadEditor = shabadEditor;

		this.shabadPlayer.addPlayEventListener(this);

		playButton.addActionListener(this);
		playButton.setActionCommand("play");

		pauseButton.addActionListener(this);
		pauseButton.setActionCommand("pause");
		pauseButton.setEnabled(false);

		stopButton.addActionListener(this);
		stopButton.setActionCommand("stop");
		stopButton.setEnabled(false);

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

		setLayout(new WrapLayout(FlowLayout.LEFT));
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

		Dimension size = getPreferredSize();

		if (shabadEditor instanceof TableShabadEditor) {
			JButton addRowButton = new JButton("+");
			addRowButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					TableShabadEditor editor = (TableShabadEditor) shabadEditor;
					int row = editor.getSelectedRow();
					editor.addRowAbove(row);
				}
			});

			JButton deleteRowButton = new JButton("-");
			deleteRowButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					TableShabadEditor editor = (TableShabadEditor) shabadEditor;
					int row = editor.getSelectedRow();
					editor.deleteRow(row);
				}
			});

			JLabel taalLabel = new JLabel("Taal (Beat):");
			String[] taalNames = new String[] { "Dadra (6 Beats)",
					"Roopak (7 Beats)", "Keherva (8 Beats)",
					"Jap Taal (10 Beats)", "Ek Taal (12 Beats)",
					"Deepchandi (14 Beats)", "Teen Taal (16 Beats)" };
			final JComboBox taalComboBox = new JComboBox(taalNames);
			taalComboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String taal = (String) taalComboBox.getSelectedItem();
					if (taal.equals("Dadra (6 Beats)")) {
						System.out.println("Taal set to Dadra");
					}
				}
			});

			add(addRowButton);
			add(deleteRowButton);
			add(taalLabel);
			add(taalComboBox);
		}

		setSize(size);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("play")) {
			if (shabadPlayer.isPaused()) {
				shabadPlayer.unpause();
			} else {
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (shabadEditor.isValidShabad()) {
							shabadPlayer.play(shabadEditor.getShabad(),
									(Double) tempoControl.getValue());
						} else {
							DialogUtility.showInvalidShabadDialog();
						}
					}
				}).start();
			}
		} else if (command.equals("pause")) {
			shabadPlayer.pause();
		} else if (command.equals("stop")) {
			shabadPlayer.stop();
		}
	}

	@Override
	public void onPlayEvent(PlayEvent e) {
		if (e == PlayEvent.PLAY) {
			playButton.setEnabled(false);
			pauseButton.setEnabled(true);
			stopButton.setEnabled(true);
			tempoControl.setEnabled(false);
			repeat.setEnabled(false);
			startField.setEnabled(false);
			endField.setEnabled(false);
			shabadEditor.setEnabled(false);
		} else if (e == PlayEvent.PAUSE) {
			playButton.setEnabled(true);
			pauseButton.setEnabled(false);
			stopButton.setEnabled(true);
			tempoControl.setEnabled(false);
			repeat.setEnabled(false);
			startField.setEnabled(false);
			endField.setEnabled(false);
			shabadEditor.setEnabled(false);
		} else if (e == PlayEvent.STOP) {
			playButton.setEnabled(true);
			pauseButton.setEnabled(false);
			stopButton.setEnabled(false);
			tempoControl.setEnabled(true);
			repeat.setEnabled(true);
			startField.setEnabled(true);
			endField.setEnabled(true);
			shabadEditor.setEnabled(true);
		}
	}

	@Override
	public void onFileEvent(FileEvent e) {
		if (e == FileEvent.OPEN) {
			startField.setText("");
			endField.setText("");
			startField.setEnabled(false);
		}
	}

}

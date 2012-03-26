package gsingh.learnkirtan;

import gsingh.learnkirtan.keys.BlackKey;
import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.WhiteKey;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

public class Main implements ActionListener, ItemListener {

	final int WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT, BLACK_KEY_WIDTH,
			BLACK_KEY_HEIGHT;
	final int WIDTH;
	final JFileChooser fc;
	{
		WHITE_KEY_WIDTH = Key.WHITE_KEY_WIDTH;
		BLACK_KEY_WIDTH = Key.BLACK_KEY_WIDTH;
		WHITE_KEY_HEIGHT = Key.WHITE_KEY_HEIGHT;
		BLACK_KEY_HEIGHT = Key.BLACK_KEY_HEIGHT;
		WIDTH = 3 * (WHITE_KEY_WIDTH * 7) + WHITE_KEY_WIDTH;
		fc = new JFileChooser();
	}

	public static Key keys[] = new Key[48];
	private static int index = 0;
	private String prevText = "";
	private boolean playing = false;

	JTextArea shabadEditor = null;
	JSpinner tempoControl;
	JFrame frame;
	File curFile;

	public Main() {
		frame = new JFrame("Learn Kirtan");
		initMenu();

		JPanel mainPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JLayeredPane pianoPanel = new JLayeredPane();

		mainPanel.setLayout(new GridBagLayout());

		JButton playButton = new JButton("Play");
		JButton pauseButton = new JButton("Pause");
		JButton stopButton = new JButton("Stop");

		playButton.addActionListener(this);
		playButton.setActionCommand("play");

		pauseButton.addActionListener(this);
		pauseButton.setActionCommand("pause");

		stopButton.addActionListener(this);
		stopButton.setActionCommand("stop");

		SpinnerNumberModel model = new SpinnerNumberModel(1, 0, 2, .1);
		tempoControl = new JSpinner(model);
		JSpinner.NumberEditor editor = (JSpinner.NumberEditor) tempoControl
				.getEditor();
		DecimalFormat format = editor.getFormat();
		format.setMinimumFractionDigits(1);
		Dimension d = tempoControl.getPreferredSize();
		d.width = 40;
		tempoControl.setPreferredSize(d);

		GridBagConstraints c = new GridBagConstraints();
		// Construct each top level component
		controlPanel.add(playButton);
		controlPanel.add(pauseButton);
		controlPanel.add(stopButton);
		controlPanel.add(tempoControl);
		shabadEditor = new JTextArea(20, 78);
		shabadEditor.setDisabledTextColor(Color.GRAY);
		constructKeyboard(pianoPanel);

		// Add the piano panel and shabad editor to the window
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		mainPanel.add(controlPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		// c.weighty = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		pianoPanel
				.setPreferredSize(new Dimension(WIDTH - 18, WHITE_KEY_HEIGHT));
		mainPanel.add(pianoPanel, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		mainPanel.add(shabadEditor, c);
		frame.add(mainPanel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, WHITE_KEY_HEIGHT * 3 + 30);
		frame.setLocation(250, 60);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	void initMenu() {

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu viewMenu = new JMenu("View");
		menuBar.add(fileMenu);
		menuBar.add(viewMenu);

		// Initialize fileMenu items
		JMenuItem createItem = new JMenuItem("Create new shabad", KeyEvent.VK_C);
		JMenuItem openItem = new JMenuItem("Open existing shabad",
				KeyEvent.VK_O);
		JMenuItem saveItem = new JMenuItem("Save current shabad", KeyEvent.VK_S);

		// Set listeners
		createItem.setActionCommand("create");
		createItem.addActionListener(this);
		openItem.setActionCommand("open");
		openItem.addActionListener(this);
		saveItem.setActionCommand("save");
		saveItem.addActionListener(this);

		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(createItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);

		// Initialize viewMenu items
		JMenuItem showEditorItem = new JMenuItem("Show shabad editor",
				KeyEvent.VK_S);

		// Set listeners
		showEditorItem.setActionCommand("showeditor");
		showEditorItem.addActionListener(this);

		viewMenu.setMnemonic(KeyEvent.VK_V);
		viewMenu.add(showEditorItem);

		frame.setJMenuBar(menuBar);
	}

	void constructKeyboard(Container panel) {
		int i = 0;
		int j = 0;

		for (int k = 0; k < 3; k++) {
			addWhiteKey(panel, i++);
			addBlackKey(panel, j++);
			addWhiteKey(panel, i++);
			addBlackKey(panel, j++);
			addWhiteKey(panel, i++);
			addWhiteKey(panel, i++);
			j++;
			addBlackKey(panel, j++);
			addWhiteKey(panel, i++);
			addBlackKey(panel, j++);
			addWhiteKey(panel, i++);
			addBlackKey(panel, j++);
			j++;
			addWhiteKey(panel, i++);
		}
	}

	void addWhiteKey(Container panel, int i) {
		WhiteKey b = new WhiteKey();
		b.setLocation(i++ * WHITE_KEY_WIDTH, 0);
		panel.add(b, 0, -1);
		keys[index++] = b;
	}

	void addBlackKey(Container panel, int factor) {
		BlackKey b = new BlackKey();
		b.setLocation(WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 + factor
				* WHITE_KEY_WIDTH, 0);
		panel.add(b, 1, -1);
		keys[index++] = b;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		System.out.println(command);

		if (command.equals("play")) {
			if (!shabadEditor.getText().equals("")) {
				if (Parser.isPaused())
					Parser.play();
				else {
					new Thread(new Runnable() {
						public void run() {
							shabadEditor.setEnabled(false);
							tempoControl.setEnabled(false);
							Parser.parseAndPlay(shabadEditor.getText(),
									(Double) tempoControl.getValue());
							shabadEditor.setEnabled(true);
							tempoControl.setEnabled(true);
						}
					}).start();
				}
				playing = true;
			} else {
				System.out.println("No Text.");
				JOptionPane.showMessageDialog(frame, "Error: Nothing to play",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (command.equals("pause")) {
			if (playing) {
				Parser.setPause();
				playing = false;
			}
		} else if (command.equals("stop")) {
			Parser.stop();
			playing = false;
		} else if (command.equals("create")) {
			int result = askForSave();
			if (result != JOptionPane.CANCEL_OPTION) {
				if (result == JOptionPane.YES_OPTION) {
					try {
						save();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				curFile = null;
				shabadEditor.setText("");
			}

		} else if (command.equals("open")) {
			int result = askForSave();
			if (result != JOptionPane.CANCEL_OPTION) {
				if (result == JOptionPane.YES_OPTION)

					try {
						save();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				openFile();
			}
		} else if (command.equals("showeditor")) {

		} else if (command.equals("save")) {
			try {
				save();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public int askForSave() {
		if (!prevText.equals(shabadEditor.getText()))
			return JOptionPane.showConfirmDialog(frame,
					"Would you like to save before proceeding?");
		else
			return -1;
	}

	public void save() throws IOException {
		if (curFile == null) {
			int returnVal = fc.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				curFile = fc.getSelectedFile();

				if (curFile.exists()) {

					int result = JOptionPane.showConfirmDialog(frame,
							"Overwrite existing file?", "Confirm Overwrite",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (result == JOptionPane.OK_OPTION)
						write();
				} else
					write();
			}
		} else
			write();
	}

	public void write() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(curFile));
		shabadEditor.write(bw);
		bw.close();
		prevText = shabadEditor.getText();
	}

	public void openFile() {
		int returnVal = fc.showOpenDialog(frame);
		BufferedReader br = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			curFile = fc.getSelectedFile();
			try {
				br = new BufferedReader(new FileReader(curFile));
				shabadEditor.read(br, "File");
				br.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		new Main();
	}
}

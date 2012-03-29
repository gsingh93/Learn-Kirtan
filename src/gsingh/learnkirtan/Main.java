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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;

public class Main implements ActionListener, ItemListener {

	/**
	 * Key dimensions
	 */
	final int WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT, BLACK_KEY_WIDTH,
			BLACK_KEY_HEIGHT;

	/**
	 * Width of the screen
	 */
	final int WIDTH;
	final JFileChooser fc;
	{
		WHITE_KEY_WIDTH = Key.WHITE_KEY_WIDTH;
		BLACK_KEY_WIDTH = Key.BLACK_KEY_WIDTH;
		WHITE_KEY_HEIGHT = Key.WHITE_KEY_HEIGHT;
		BLACK_KEY_HEIGHT = Key.BLACK_KEY_HEIGHT;
		WIDTH = 3 * (WHITE_KEY_WIDTH * 7) + WHITE_KEY_WIDTH;
		fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("SBD (Shabad) File",
				"sbd");
		fc.setFileFilter(filter);
	}

	/**
	 * Stores all of the keys on the keyboard
	 */
	public static Key keys[] = new Key[48];

	/**
	 * The index used when adding the keys to the keyboard
	 */
	private static int index = 0;

	/**
	 * Used to determine whether a save is necessary. The text in
	 * {@code shabadEditor} is compared to this string and if they don't match,
	 * a save is necessary.
	 */
	private String prevText = "";

	/**
	 * True if a shabad is currently playing, false otherwise. At the moment,
	 * it's only use is to determine whether pause should set the pause variable
	 * or not.
	 */
	private boolean playing = false;

	/**
	 * The main shabad editor. When play is pressed, the text in here will be
	 * played. It cannot be edited while playing.
	 */
	JTextArea shabadEditor = null;

	/**
	 * A spinner controlling tempo. It is set to 1.0 by default, has an
	 * increment of 0.1, and has a range from .1 to 2. The shabad plays at this
	 * multiplier times the default speed (in the implementation, the length of
	 * each keypress is divided by this value to have the same effect). It
	 * cannot be changed while playing.
	 */
	JSpinner tempoControl;

	/**
	 * The file in which your shabad will be saved or was opened from. When the
	 * program is first started, it has the value of {@code null}.
	 */
	File curFile;
	JFrame frame;

	public Main() {

		// Make sure the soundbank is installed
		System.out.println(installSoundBank());

		frame = new JFrame("Learn Kirtan v0.1 Beta");
		initMenu();

		JPanel mainPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JLayeredPane pianoPanel = new JLayeredPane();

		mainPanel.setLayout(new GridBagLayout());

		// Construct each top level component
		initControlPanel(controlPanel);

		shabadEditor = new JTextArea(20, 78);
		shabadEditor.setDisabledTextColor(Color.GRAY);

		constructKeyboard(pianoPanel);

		initMainPanel(mainPanel, controlPanel, pianoPanel);
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
		menuBar.add(fileMenu);

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

		frame.setJMenuBar(menuBar);
	}

	void initControlPanel(JPanel controlPanel) {
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

		controlPanel.add(playButton);
		controlPanel.add(pauseButton);
		controlPanel.add(stopButton);
		controlPanel.add(tempoLabel);
		controlPanel.add(tempoControl);
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

	void initMainPanel(JPanel mainPanel, JPanel controlPanel,
			JLayeredPane pianoPanel) {
		GridBagConstraints c = new GridBagConstraints();

		// Add the piano panel and shabad editor to the window
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		mainPanel.add(controlPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
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
				String filename = curFile.getName();
				if (filename.length() <= 4) {
					filename = filename + ".sbd";
					System.out.println(filename);
					curFile = new File(curFile.getAbsolutePath() + ".sbd");
				} else if (!filename.substring(filename.length() - 4).equals(
						".sbd")) {
					filename = filename + ".sbd";
					curFile = new File(curFile.getAbsolutePath() + ".sbd");
				}
				System.out.println(filename);
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

	public boolean installSoundBank() {

		// Determine where the JRE is installed
		File file = new File("C:\\Program Files (x86)\\Java\\jre6");
		if (!file.exists()) {
			file = new File("C:\\Program Files\\Java\\jre6");
			if (!file.exists())
				return false;
		}

		// If the JRE is properly installed, check if the SoundBank is already
		// installed
		file = new File(file.getAbsolutePath()
				+ "\\lib\\audio\\soundbank-min.gm");
		if (!file.exists()) {
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("soundbank-min.gm");
			OutputStream os = null;
			try {
				os = new FileOutputStream(file.getAbsolutePath());
				IOUtils.copy(is, os);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return true;
	}

	public static void main(String[] args) {
		new Main();
	}
}

package gsingh.learnkirtan;

import gsingh.learnkirtan.keys.BlackKey;
import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.WhiteKey;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;

public class Main {

	public static final String VERSION = "0.3.1";

	public static final String BASETITLE = "Learn Kirtan v" + VERSION
			+ " Beta - ";

	/**
	 * The logger for this class
	 */
	private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

	/**
	 * The {@link FileHandler} to which log messages are written
	 */
	public static FileHandler logFile;

	/**
	 * Width of the screen
	 */
	private final int WIDTH = 3 * (Key.WHITE_KEY_WIDTH * 7)
			+ Key.WHITE_KEY_WIDTH - 20;

	/**
	 * The filechooser used for opening and saving files
	 */
	private final JFileChooser fc;
	{
		fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("SBD (Shabad) File",
				"sbd");
		fc.setFileFilter(filter);
	}

	/**
	 * Stores all of the keys on the keyboard
	 */
	public static Key keys[] = new Key[36];

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
	 * The main shabad editor. When play is pressed, the text in here will be
	 * played. It cannot be edited while playing.
	 */
	private JTextArea shabadEditor = null;

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

	/**
	 * The file in which your shabad will be saved or was opened from. When the
	 * program is first started, it has the value of {@code null}.
	 */
	private File curFile;

	/**
	 * The main frame
	 */
	private JFrame frame;

	/**
	 * Action listener for all events
	 */
	private ActionListenerClass listener = new ActionListenerClass();

	public static void main(String[] args) {

		// Set up logging
		LOGGER.setLevel(Level.INFO);
		try {
			logFile = new FileHandler("log");
			SimpleFormatter formatter = new SimpleFormatter();
			logFile.setFormatter(formatter);
			LOGGER.addHandler(logFile);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
	}

	public Main() {

		// Make sure the soundbank is installed
		if (!installSoundBank()) {
			JOptionPane
					.showMessageDialog(
							frame,
							"Error: There may be an issue with your Java installation"
									+ " or the required file dependencies could not be installed."
									+ " Sound may not work. If the problem persists,"
									+ " please contact the developer for assistance.",
							"Error", JOptionPane.ERROR_MESSAGE);
		}

		// Check for updates
		checkForUpdate();

		createAndShowGui();
	}

	/**
	 * Installs the soundbank file in the JRE lib/audio folder
	 * 
	 * @return - true if file is present or was installed correctly. False if
	 *         there was an error.
	 */
	public boolean installSoundBank() {
		// Determine where the JRE is installed
		File file = new File("C:\\Program Files (x86)\\Java\\jre6");
		if (!file.exists()) {
			LOGGER.warning("C:\\Program Files (x86)\\Java\\jre6 does not exist.");
			file = new File("C:\\Program Files\\Java\\jre6");
			if (!file.exists()) {
				LOGGER.severe("C:\\Program Files\\Java\\jre6 does not exist.");
				return false;
			}
		}

		// If the JRE is properly installed, check if the SoundBank is already
		// installed
		file = new File(file.getAbsolutePath()
				+ "\\lib\\audio\\soundbank-min.gm");
		if (!file.exists()) {
			LOGGER.warning("soundbank-min.gm does not exist.");
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("soundbank-min.gm");
			OutputStream os = null;
			try {
				os = new FileOutputStream(file.getAbsolutePath());
				IOUtils.copy(is, os);
			} catch (IOException e) {
				LOGGER.severe("An IOException was thrown when installing the soundbank.");
				e.printStackTrace();
				return false;
			}
		} else {
			LOGGER.info("Soundbank found.");
		}

		LOGGER.info("Soundbank installation successful.");
		return true;
	}

	/**
	 * Connects to the server to see if there is a later update. If found, offer
	 * to go to download page
	 */
	public void checkForUpdate() {
		String url = "http://michigangurudwara.com/version.txt";
		String updateSite = "https://github.com/gsingh93/Learn-Kirtan/wiki";
		String line = VERSION;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new URL(url).openStream()));
			line = reader.readLine();

			if (!line.equals(VERSION)) {
				int result = JOptionPane
						.showOptionDialog(
								frame,
								"The software has detected that an update is available. Would you like to go to the download page?",
								"Update Available", JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null, null,
								null);

				LOGGER.info("Update Available");

				if (result == JOptionPane.YES_OPTION)
					Desktop.getDesktop().browse(new URI(updateSite));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private void createAndShowGui() {
		frame = new JFrame(BASETITLE + "Untitled Shabad");
		initMenu();

		JPanel mainPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JLayeredPane pianoPanel = new JLayeredPane();

		mainPanel.setLayout(new GridBagLayout());

		// Construct each top level component
		initControlPanel(controlPanel);

		shabadEditor = new JTextArea(16, 60);
		shabadEditor.setDisabledTextColor(Color.GRAY);
		shabadEditor.setFont(new Font("Dialog", Font.BOLD, 16));
		shabadEditor.addKeyListener(listener);

		constructKeyboard(pianoPanel);

		initMainPanel(mainPanel, controlPanel, pianoPanel);
		frame.add(mainPanel);

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				int result = askForSave();
				if (result != JOptionPane.CANCEL_OPTION
						&& result != JOptionPane.CLOSED_OPTION) {
					if (result == JOptionPane.OK_OPTION)
						try {
							save();
						} catch (IOException e) {
							e.printStackTrace();
						}
					LOGGER.info("Application closed.");
					System.exit(0);
				} else {
					LOGGER.info("Application closed.");
					System.exit(0);
				}
			}
		});
		frame.setSize(WIDTH, Key.WHITE_KEY_HEIGHT * 3 + 30);
		frame.setLocation(250, 60);
		frame.setResizable(false);
		frame.setVisible(true);

		shabadEditor.requestFocusInWindow();
	}

	/**
	 * Initializes the menu bar
	 */
	private void initMenu() {
		LOGGER.fine("Menu initialization started.");

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu optionsMenu = new JMenu("Options");
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(fileMenu);
		menuBar.add(optionsMenu);
		menuBar.add(helpMenu);

		// Initialize fileMenu items
		JMenuItem createItem = new JMenuItem("Create new shabad", KeyEvent.VK_C);
		JMenuItem openItem = new JMenuItem("Open existing shabad",
				KeyEvent.VK_O);
		JMenuItem saveItem = new JMenuItem("Save current shabad", KeyEvent.VK_S);

		// Initialize optionsMenu items
		JMenuItem saItem = new JMenuItem("Change Sa Key", KeyEvent.VK_C);

		// Intialize helpMenu items
		JMenuItem helpItem = new JMenuItem("Help", KeyEvent.VK_H);
		JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);

		// Set listeners
		createItem.setActionCommand("create");
		createItem.addActionListener(listener);
		createItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		openItem.setActionCommand("open");
		openItem.addActionListener(listener);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		saveItem.setActionCommand("save");
		saveItem.addActionListener(listener);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		saItem.setActionCommand("changesa");
		saItem.addActionListener(listener);

		helpItem.setActionCommand("help");
		helpItem.addActionListener(listener);
		helpItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
		aboutItem.setActionCommand("about");
		aboutItem.addActionListener(listener);

		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(createItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);

		optionsMenu.setMnemonic(KeyEvent.VK_O);
		optionsMenu.add(saItem);

		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.add(helpItem);
		helpMenu.add(aboutItem);

		frame.setJMenuBar(menuBar);

		LOGGER.fine("Menu initialization completed.");
	}

	/**
	 * Initializes the control panel
	 * 
	 * @param controlPanel
	 *            - the panel to initialize
	 */
	void initControlPanel(JPanel controlPanel) {
		LOGGER.fine("Control panel initialization started.");

		JButton playButton = new JButton("Play");
		JButton pauseButton = new JButton("Pause");
		JButton stopButton = new JButton("Stop");

		playButton.addActionListener(listener);
		playButton.setActionCommand("play");

		pauseButton.addActionListener(listener);
		pauseButton.setActionCommand("pause");

		stopButton.addActionListener(listener);
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
		repeat.addItemListener(listener);

		JLabel startLabel = new JLabel("Start Label:");
		JLabel endLabel = new JLabel("End Label:");
		startField = new JTextField(7);
		endField = new JTextField(7);

		controlPanel.add(playButton);
		controlPanel.add(pauseButton);
		controlPanel.add(stopButton);
		controlPanel.add(tempoLabel);
		controlPanel.add(tempoControl);
		controlPanel.add(repeat);
		controlPanel.add(startLabel);
		controlPanel.add(startField);
		controlPanel.add(endLabel);
		controlPanel.add(endField);

		LOGGER.fine("Control panel initialization completed.");
	}

	/**
	 * Constructs the piano
	 * 
	 * @param panel
	 *            - the layer in which to construct the piano
	 */
	void constructKeyboard(Container panel) {
		LOGGER.fine("Keyboard construction started.");

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

		LOGGER.fine("Keyboard construction completed.");
	}

	/**
	 * Adds a white key to the piano panel
	 * 
	 * @param panel
	 *            - the panel to which to add the key
	 * @param i
	 *            - a number which is used to calculate the position of the key
	 */
	void addWhiteKey(Container panel, int i) {
		WhiteKey b = new WhiteKey();
		b.setLocation(i++ * Key.WHITE_KEY_WIDTH, 0);
		panel.add(b, 0, -1);
		keys[index++] = b;
	}

	/**
	 * Adds a black key to the piano panel
	 * 
	 * @param panel
	 *            - the panel to which to add the key
	 * @param factor
	 *            - a number which is used to calculate the position of the key
	 */
	void addBlackKey(Container panel, int factor) {
		BlackKey b = new BlackKey();
		b.setLocation(Key.WHITE_KEY_WIDTH - Key.BLACK_KEY_WIDTH / 2 + factor
				* Key.WHITE_KEY_WIDTH, 0);
		panel.add(b, 1, -1);
		keys[index++] = b;
	}

	/**
	 * Constructs the mainPanel by taking the {@code controlPanel},
	 * {@code pianoPanel}, and {@code shabadEditor} and arranging them using a
	 * {@link GridBagLayout}.
	 * 
	 */
	void initMainPanel(JPanel mainPanel, JPanel controlPanel,
			JLayeredPane pianoPanel) {
		LOGGER.fine("Main panel initialization started.");

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
		pianoPanel.setPreferredSize(new Dimension(WIDTH - 18,
				Key.WHITE_KEY_HEIGHT));
		mainPanel.add(pianoPanel, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		mainPanel.add(new JScrollPane(shabadEditor), c);

		LOGGER.fine("Main panel initialization completed.");
	}

	/**
	 * Enables or disables certain inputs depending on the value of bool. Used
	 * when the play button is pressed.
	 * 
	 * @param bool
	 *            - true if the inputs should be enabled, false if they should
	 *            be disabled.
	 */
	public void setInputBoxes(boolean bool) {
		LOGGER.info("Input boxes " + (bool ? "enabled." : "disabled."));

		shabadEditor.setEnabled(bool);
		tempoControl.setEnabled(bool);
		repeat.setEnabled(bool);
		startField.setEnabled(bool);
		endField.setEnabled(bool);
	}

	/**
	 * Prompts the user if they would like to save if their text has been edited
	 * 
	 * @return a number specifying which option the user chose. -1 if the user
	 *         was not prompted
	 */
	public int askForSave() {
		if (!prevText.equals(shabadEditor.getText())) {
			LOGGER.info("User prompted to save.");
			return JOptionPane.showConfirmDialog(frame,
					"Would you like to save before proceeding?");
		} else {
			LOGGER.info("Save is not necessary. Continuing without save.");
			return -1;
		}
	}

	/**
	 * Saves the text in the shabadEditor to the file specified. If no file is
	 * specified, the user is prompted to specify one.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		LOGGER.info("Save process started.");
		if (curFile == null) {
			LOGGER.info("User will be prompted for a save location.");
			int returnVal = fc.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				curFile = fc.getSelectedFile();
				String filename = curFile.getName();
				if (filename.length() <= 4) {
					filename = filename + ".sbd";
					curFile = new File(curFile.getAbsolutePath() + ".sbd");
					LOGGER.info(".sbd extension was automatically appended.");
				} else if (!filename.substring(filename.length() - 4).equals(
						".sbd")) {
					filename = filename + ".sbd";
					curFile = new File(curFile.getAbsolutePath() + ".sbd");
					LOGGER.info(".sbd extension was supplied.");
				}
				LOGGER.info("Filename Chosen: " + filename);
				if (curFile.exists()) {
					LOGGER.info("File specified already exists.");
					int result = JOptionPane.showConfirmDialog(frame,
							"Overwrite existing file?", "Confirm Overwrite",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (result == JOptionPane.OK_OPTION) {
						LOGGER.warning("User chose to overwrite.");
						write();
						frame.setTitle(BASETITLE + curFile.getName());
					} else {
						LOGGER.info("User chose not to overwrite.");
					}
				} else {
					LOGGER.info("User specified a new file. Proceeding with save.");
					write();
					frame.setTitle(BASETITLE + curFile.getName());
				}
			}
		} else {
			LOGGER.info("User is saving to an already chosen file.");
			write();
		}
	}

	/**
	 * Write the shabadEditor text to {@code curFile}
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException {
		LOGGER.fine("File write started.");
		BufferedWriter bw = new BufferedWriter(new FileWriter(curFile));
		shabadEditor.write(bw);
		bw.close();
		prevText = shabadEditor.getText();
		if (frame.getTitle().contains("*")) {
			frame.setTitle(frame.getTitle().substring(0,
					frame.getTitle().length() - 1));
		}
		LOGGER.fine("File write completed.");
	}

	/**
	 * Prompts the user for a file to open and opens the selected file
	 */
	public void openFile() {
		LOGGER.fine("File open process started.");
		int returnVal = fc.showOpenDialog(frame);
		BufferedReader br = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			curFile = fc.getSelectedFile();
			LOGGER.info("File Chosen: " + curFile.getName());
			if (curFile.exists()) {
				try {
					LOGGER.fine("File open started");
					br = new BufferedReader(new FileReader(curFile));
					shabadEditor.read(br, "File");
					br.close();
					prevText = shabadEditor.getText();
					frame.setTitle(BASETITLE + curFile.getName());
					startField.setText("");
					endField.setText("");
					shabadEditor.requestFocusInWindow();
					LOGGER.fine("File write completed.");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			} else {
				LOGGER.info("File doesn't exist.");
				JOptionPane.showMessageDialog(frame,
						"Error: File doesn't exist.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		LOGGER.fine("File open process finished.");
	}

	class ActionListenerClass extends KeyAdapter implements ActionListener,
			ItemListener {
		/**
		 * True if a shabad is currently playing, false otherwise. At the
		 * moment, it's only use is to determine whether pause should set the
		 * pause variable or not.
		 */
		private boolean playing = false;

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			LOGGER.info("Action Performed: " + command);

			if (command.equals("play")) {
				if (!Parser.isPlaying()) {
					if (!shabadEditor.getText().equals("")) {
						if (Parser.isPaused()) {
							LOGGER.info("Playback unpaused.");
							Parser.play();
						} else {
							new Thread(new Runnable() {
								public void run() {
									LOGGER.info("Starting playback.");
									setInputBoxes(false);
									Parser.parseAndPlay(shabadEditor.getText(),
											startField.getText(),
											endField.getText(),
											(Double) tempoControl.getValue());
									setInputBoxes(true);
								}
							}).start();
						}
						playing = true;
					} else {
						LOGGER.warning("The user presed play when there was no text in input box");
						JOptionPane.showMessageDialog(frame,
								"Error: Nothing to play", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					LOGGER.warning("The user presed play when shabad was already playing");
					JOptionPane.showMessageDialog(frame,
							"The shabad is already playing.", "Error",
							JOptionPane.ERROR_MESSAGE);
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
				if (result != JOptionPane.CANCEL_OPTION
						&& result != JOptionPane.CLOSED_OPTION || result == -1) {
					if (result == JOptionPane.YES_OPTION) {
						try {
							save();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

					frame.setTitle(BASETITLE + "Untitled Shabad");
					curFile = null;
					shabadEditor.setText("");
				}

			} else if (command.equals("open")) {
				int result = askForSave();
				if (result != JOptionPane.CANCEL_OPTION
						&& result != JOptionPane.CLOSED_OPTION || result == -1) {
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
			} else if (command.equals("changesa")) {
				SpinnerModel saModel = new SpinnerNumberModel(
						Parser.getSaKey() + 1, 1, 36, 1);
				JSpinner saSpinner = new JSpinner(saModel);

				JPanel panel = new JPanel();
				panel.add(new JLabel("Choose the key number for sa:"));
				panel.add(saSpinner);

				int result = JOptionPane.showConfirmDialog(frame, panel,
						"Change Sa Key", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (result == JOptionPane.OK_OPTION) {
					int value = (Integer) saSpinner.getValue();

					int difference = value - Parser.getSaKey() - 1;

					LOGGER.info("Sa key changed to: " + value);
					Parser.setSaKey(value);

					if (difference > 0) {
						for (int i = 0; i < difference; i++) {
							Key.notes.add(0,
									Key.notes.get(Key.notes.size() - 1));
						}
					} else if (difference < 0) {
						for (int i = 0; i < -1 * difference; i++) {
							Key.notes.add(Key.notes.size(), Key.notes.get(0));
						}
					}

					// Relabel the keys
					for (Key key : keys) {
						key.label();
						if (key instanceof BlackKey) {
							if (key.getText().contains("Dha")) {
								key.setFont(new Font("Dialog", Font.PLAIN, 7));
							} else {
								key.setFont(new Font("Dialog", Font.PLAIN, 9));
							}
						}
					}
				}

			} else if (command.equals("help")) {
				new HelpFrame();
			} else if (command.equals("about")) {
				JOptionPane
						.showConfirmDialog(
								frame,
								"This software was written by Gulshan Singh (gulshan@umich.edu) and is free \n"
										+ "and opensource under the Apache License. Please contact me if you would like to contribute.\n"
										+ "\n Version " + VERSION, "About",
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.INFORMATION_MESSAGE);
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if (!e.isAltDown() && !e.isControlDown()) {
				String title = frame.getTitle();
				if (!title.contains("*"))
					frame.setTitle(frame.getTitle() + "*");
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();
			LOGGER.info(source.getClass().getName()
					+ ((e.getStateChange() == ItemEvent.SELECTED) ? " selected."
							: " deselected."));

			if (source == repeat) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					Parser.setRepeat(true);
				else
					Parser.setRepeat(false);
			}
		}
	}
}

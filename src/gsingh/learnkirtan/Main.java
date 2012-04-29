package gsingh.learnkirtan;

import static gsingh.learnkirtan.Constants.BLACK_KEY_WIDTH;
import static gsingh.learnkirtan.Constants.MAX_KEYS;
import static gsingh.learnkirtan.Constants.WHITE_KEY_HEIGHT;
import static gsingh.learnkirtan.Constants.WHITE_KEY_WIDTH;
import gsingh.learnkirtan.Constants.Octave;
import gsingh.learnkirtan.keys.BlackKey;
import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.WhiteKey;
import gsingh.learnkirtan.parser.Parser;
import gsingh.learnkirtan.utility.FileUtility;
import gsingh.learnkirtan.utility.NetworkUtility;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.io.IOUtils;

public class Main {

	public static final String VERSION = "0.3.1";

	public static final String BASETITLE = "Learn Kirtan v" + VERSION
			+ " Beta - ";

	/**
	 * The logger for this class
	 */
	private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

	private static enum Mode {
		EDIT, COMPOSE
	}

	private enum SpaceKey {
		SPACE, BACKSPACE
	}

	/**
	 * The {@link FileHandler} to which log messages are written
	 */
	public static FileHandler logFile;

	/**
	 * Width of the screen
	 */
	private final int WIDTH = 3 * (WHITE_KEY_WIDTH * 7) + WHITE_KEY_WIDTH - 62;

	/**
	 * Stores all of the keys on the keyboard
	 */
	public static Key keys[] = new Key[MAX_KEYS];

	/**
	 * The index used when adding the keys to the keyboard
	 */
	private static int index = 0;

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
	 * Settings Manager for this application
	 */
	private SettingsManager settingsManager;

	public static Main main;

	private UndoManager undo = new UndoManager();
	private UndoAction undoAction = new UndoAction();
	private RedoAction redoAction = new RedoAction();

	private Mode mode = Mode.EDIT;
	private Octave octave = Octave.MIDDLE;

	private boolean sargamLabeled = true;
	private boolean keyLabeled = true;

	public static void main(String[] args) {

		// Set up logging
		LOGGER.setLevel(Level.INFO);
		try {
			Calendar cal = Calendar.getInstance();
			logFile = new FileHandler("log\\log_" + cal.get(Calendar.YEAR)
					+ "_" + cal.get(Calendar.MONTH) + "_"
					+ cal.get(Calendar.DAY_OF_MONTH) + "_"
					+ cal.get(Calendar.HOUR_OF_DAY) + "_"
					+ cal.get(Calendar.MINUTE) + "_" + cal.get(Calendar.SECOND));
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
				main = new Main();

				// Check for updates
				NetworkUtility.checkForUpdate(VERSION);
			}
		});
	}

	public static Main getMain() {
		return main;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
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

		settingsManager = new SettingsManager();
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

	private void createAndShowGui() {
		frame = new JFrame(BASETITLE + "Untitled Shabad");
		initMenu();

		JPanel mainPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JLayeredPane pianoPanel = new JLayeredPane();

		mainPanel.setLayout(new GridBagLayout());

		// Construct each top level component
		initControlPanel(controlPanel);

		ShabadEditorListener listener = new ShabadEditorListener();

		shabadEditor = new JTextArea(16, 57);
		shabadEditor.setDisabledTextColor(Color.GRAY);
		shabadEditor.setFont(new Font("Dialog", Font.BOLD, 16));
		shabadEditor.getDocument().addDocumentListener(listener);
		shabadEditor.getDocument().addUndoableEditListener(listener);
		shabadEditor.addKeyListener(new KeyboardListener());
		shabadEditor.addFocusListener(new EditorFocusListener());

		InputMap inputMap = shabadEditor.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap actionMap = shabadEditor.getActionMap();

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
				SpaceKey.SPACE);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
				SpaceKey.BACKSPACE);
		actionMap.put(SpaceKey.SPACE, new KeyAction(shabadEditor,
				SpaceKey.SPACE));
		actionMap.put(SpaceKey.BACKSPACE, new KeyAction(shabadEditor,
				SpaceKey.BACKSPACE));

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
						save();
					LOGGER.info("Application closed.");
					System.exit(0);
				} else {
					LOGGER.info("Application close canceled.");
				}

				if (result == -1) {
					LOGGER.info("Application closed.");
					System.exit(0);
				}
			}
		});
		frame.setSize(WIDTH, WHITE_KEY_HEIGHT * 3 + 30);
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
		JMenu editMenu = new JMenu("Edit");
		JMenu optionsMenu = new JMenu("Options");
		JMenu keyboardMenu = new JMenu("Keyboard Mode");
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(keyboardMenu);
		menuBar.add(optionsMenu);
		menuBar.add(helpMenu);

		// Initialize fileMenu items
		JMenuItem createItem = new JMenuItem("Create new shabad", KeyEvent.VK_C);
		JMenuItem openItem = new JMenuItem("Open existing shabad",
				KeyEvent.VK_O);
		JMenuItem saveItem = new JMenuItem("Save current shabad", KeyEvent.VK_S);

		// Initialize editMenu
		JMenuItem undoItem = new JMenuItem(undoAction);
		JMenuItem redoItem = new JMenuItem(redoAction);
		JMenuItem cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
		JMenuItem copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
		JMenuItem pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());

		// Initialize optionsMenu items
		JMenuItem saItem = new JMenuItem("Change Sa Key", KeyEvent.VK_C);
		JCheckBoxMenuItem showSargamLabelsItem = new JCheckBoxMenuItem(
				"Show Sargam Labels");
		JCheckBoxMenuItem showKeyboardLabelsItem = new JCheckBoxMenuItem(
				"Show Keyboard Labels");
		showSargamLabelsItem.setSelected(true);
		showKeyboardLabelsItem.setSelected(true);

		// Initialize KeyboardMenu items
		JMenuItem composeItem = new JMenuItem("Compose", KeyEvent.VK_C);
		JMenuItem editItem = new JMenuItem("Edit", KeyEvent.VK_E);

		// Intialize helpMenu items
		JMenuItem helpItem = new JMenuItem("Help", KeyEvent.VK_H);
		JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);

		// Set listeners
		FileMenuListener l1 = new FileMenuListener();
		createItem.setActionCommand("create");
		createItem.addActionListener(l1);
		createItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		openItem.setActionCommand("open");
		openItem.addActionListener(l1);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		saveItem.setActionCommand("save");
		saveItem.addActionListener(l1);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		undoItem.setMnemonic(KeyEvent.VK_U);
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				ActionEvent.CTRL_MASK));
		undoItem.setMnemonic(KeyEvent.VK_R);
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				ActionEvent.CTRL_MASK));
		cutItem.setText("Cut");
		copyItem.setText("Copy");
		pasteItem.setText("Paste");

		OptionsMenuListener l2 = new OptionsMenuListener(showSargamLabelsItem,
				showKeyboardLabelsItem);
		saItem.setActionCommand("changesa");
		saItem.addActionListener(l2);
		showSargamLabelsItem.setActionCommand("sargamlabels");
		showSargamLabelsItem.addItemListener(l2);
		showKeyboardLabelsItem.setActionCommand("keyboardlabels");
		showKeyboardLabelsItem.addItemListener(l2);

		KeyboardMenuListener l3 = new KeyboardMenuListener();
		composeItem.setActionCommand("composemode");
		composeItem.addActionListener(l3);
		composeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.ALT_MASK));
		editItem.setActionCommand("editmode");
		editItem.addActionListener(l3);
		editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.ALT_MASK));

		helpItem.setActionCommand("help");

		// 1. create HelpSet and HelpBroker objects
		HelpSet hs = getHelpSet("Sample.hs");
		HelpBroker hb = hs.createHelpBroker();

		// 2. assign help to components
		CSH.setHelpIDString(helpItem, "top");

		// 3. handle events
		helpItem.addActionListener(new CSH.DisplayHelpFromSource(hb));
		HelpMenuListener l4 = new HelpMenuListener();
		helpItem.setAccelerator(KeyStroke.getKeyStroke("F1"));

		aboutItem.setActionCommand("about");
		aboutItem.addActionListener(l4);

		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(createItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);

		editMenu.setMnemonic(KeyEvent.VK_D);
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);

		optionsMenu.setMnemonic(KeyEvent.VK_O);
		optionsMenu.add(saItem);
		optionsMenu.add(showSargamLabelsItem);
		optionsMenu.add(showKeyboardLabelsItem);

		keyboardMenu.setMnemonic(KeyEvent.VK_K);
		keyboardMenu.add(composeItem);
		keyboardMenu.add(editItem);

		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.add(helpItem);
		helpMenu.add(aboutItem);

		frame.setJMenuBar(menuBar);

		LOGGER.fine("Menu initialization completed.");
	}

	/**
	 * find the helpset file and create a HelpSet object
	 */
	public HelpSet getHelpSet(String helpsetfile) {
		HelpSet hs = null;
		ClassLoader cl = this.getClass().getClassLoader();
		try {
			URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
			hs = new HelpSet(null, hsURL);
		} catch (Exception ee) {
			LOGGER.warning("HelpSet: " + ee.getMessage());
			LOGGER.warning("HelpSet: " + helpsetfile + " not found");
		}
		return hs;
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

		ButtonListener l1 = new ButtonListener();

		playButton.addActionListener(l1);
		playButton.setActionCommand("play");

		pauseButton.addActionListener(l1);
		pauseButton.setActionCommand("pause");

		stopButton.addActionListener(l1);
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

		CheckBoxListener l2 = new CheckBoxListener();
		repeat = new JCheckBox("Repeat");
		repeat.addItemListener(l2);

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
		if (index < MAX_KEYS) {
			WhiteKey b = new WhiteKey(settingsManager.getSaKey());
			b.setLocation(i++ * WHITE_KEY_WIDTH, 0);
			panel.add(b, 0, -1);
			keys[index++] = b;
		}
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
		if (index < MAX_KEYS) {
			BlackKey b = new BlackKey(settingsManager.getSaKey());
			b.setLocation(WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 + factor
					* WHITE_KEY_WIDTH, 0);
			panel.add(b, 1, -1);
			keys[index++] = b;
		}
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
		pianoPanel
				.setPreferredSize(new Dimension(WIDTH - 18, WHITE_KEY_HEIGHT));
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
	private int askForSave() {
		if (frame.getTitle().contains("*")) {
			LOGGER.info("User prompted to save.");
			return JOptionPane.showConfirmDialog(frame,
					"Would you like to save before proceeding?");
		} else {
			LOGGER.info("Save is not necessary. Continuing without save.");
			return -1;
		}
	}

	private void save() {
		int success = -1;
		try {
			success = FileUtility.save(shabadEditor, curFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// If file was successfully written, remove modified symbol
		if (success == 1 || success == 2) {
			if (frame.getTitle().contains("*")) {
				frame.setTitle(frame.getTitle().substring(0,
						frame.getTitle().length() - 1));
			}
		}

		// If the file was save for the first time, add the filename to the
		// title
		if (success == 2) {
			frame.setTitle(BASETITLE + curFile.getName());
		}
	}

	public void notePressed(KeyEvent e) {
		final int key = letterToKey(String.valueOf(e.getKeyChar())
				.toUpperCase());
		if (key < MAX_KEYS && key >= 0) {
			new Thread(new Runnable() {
				public void run() {
					keys[key].playOnce(500);
				}
			}).start();
		} else {
			LOGGER.warning("User pressed key in play mode that"
					+ " is not playable.");
		}
	}

	class ButtonListener implements ActionListener {

		// True if a shabad is currently playing, false otherwise.
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
							keys[0].requestFocusInWindow();
							new Thread(new Runnable() {
								public void run() {
									LOGGER.info("Starting playback.");
									setInputBoxes(false);
									Parser.parseAndPlay(shabadEditor.getText(),
											startField.getText(),
											endField.getText(),
											(Double) tempoControl.getValue(),
											settingsManager.getSaKey());
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
			}
		}

	}

	class FileMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			LOGGER.info("Action Performed: " + command);
			if (command.equals("open")) {
				int result = askForSave();
				if (result != JOptionPane.CANCEL_OPTION
						&& result != JOptionPane.CLOSED_OPTION || result == -1) {
					if (result == JOptionPane.YES_OPTION)
						save();

					File tempFile = curFile;
					if ((curFile = FileUtility.openFile(shabadEditor, curFile)) != null) {
						frame.setTitle(BASETITLE + curFile.getName());
						startField.setText("");
						endField.setText("");
						shabadEditor.requestFocusInWindow();
						shabadEditor.setCaretPosition(0);
						undo.discardAllEdits();
						undoAction.setEnabled(false);
					} else {
						curFile = tempFile;
					}
				}
			} else if (command.equals("save")) {
				save();
			} else if (command.equals("create")) {
				int result = askForSave();
				if (result != JOptionPane.CANCEL_OPTION
						&& result != JOptionPane.CLOSED_OPTION || result == -1) {
					if (result == JOptionPane.YES_OPTION) {
						save();
					}

					frame.setTitle(BASETITLE + "Untitled Shabad");
					curFile = null;
					shabadEditor.setText("");
					undo.discardAllEdits();
					undoAction.setEnabled(false);

					if (frame.getTitle().contains("*")) {
						frame.setTitle(frame.getTitle().substring(0,
								frame.getTitle().length() - 1));
					}
				}

			}
		}
	}

	class OptionsMenuListener implements ActionListener, ItemListener {

		JCheckBoxMenuItem sargamLabels;
		JCheckBoxMenuItem keyboardLabels;

		public OptionsMenuListener(JCheckBoxMenuItem item1,
				JCheckBoxMenuItem item2) {
			sargamLabels = item1;
			keyboardLabels = item2;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			LOGGER.info("Action Performed: " + command);

			if (command.equals("changesa")) {
				SpinnerModel saModel = new SpinnerNumberModel(
						settingsManager.getSaKey() + 1, 1, MAX_KEYS, 1);
				JSpinner saSpinner = new JSpinner(saModel);

				JPanel panel = new JPanel();
				panel.add(new JLabel("Choose the key number for sa:"));
				panel.add(saSpinner);

				int result = JOptionPane.showConfirmDialog(frame, panel,
						"Change Sa Key", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (result == JOptionPane.OK_OPTION) {
					int value = (Integer) saSpinner.getValue();

					int difference = value - settingsManager.getSaKey() - 1;

					if (difference > 0) {
						for (int i = 0; i < difference; i++) {
							Key.notes.add(0,
									Key.notes.remove((Key.notes.size() - 1)));
						}
					} else if (difference < 0) {
						for (int i = 0; i < -1 * difference; i++) {
							Key.notes.add(Key.notes.size() - 1,
									Key.notes.remove(0));
						}
					}

					// Relabel the keys
					for (Key key : keys) {
						if (sargamLabeled)
							key.labelSargamNote();
						if (key instanceof BlackKey) {
							if (key.getText().contains("Dha")) {
								key.setFont(new Font("Dialog", Font.PLAIN, 7));
							} else {
								key.setFont(new Font("Dialog", Font.PLAIN, 9));
							}
						}
					}

					LOGGER.info("Sa key changed to: " + value);
					settingsManager.setSaKey(value);
				}
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();
			if (source == sargamLabels) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					for (Key key : keys) {
						key.labelSargamNote();
						sargamLabeled = true;
					}
				} else {
					for (Key key : keys) {
						key.clearSargamNote();
						sargamLabeled = false;
					}
				}
			} else if (source == keyboardLabels) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					for (Key key : keys) {
						key.labelKeyboardNote(octave);
						keyLabeled = true;
					}
				} else {
					for (Key key : keys) {
						key.clearKeyboardNote(octave);
						keyLabeled = false;
					}
				}
			}

		}
	}

	class KeyboardMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("composemode")) {
				mode = Mode.COMPOSE;
				shabadEditor.setEditable(false);
			} else if (command.equals("editmode")) {
				mode = Mode.EDIT;
				shabadEditor.setEditable(true);
			}
		}
	}

	class EditorFocusListener extends FocusAdapter {
		@Override
		public void focusGained(FocusEvent e) {
			shabadEditor.getCaret().setVisible(true);
		}
	}

	class KeyboardListener extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {
			if (!e.isAltDown() && !e.isControlDown()) {
				if (mode == Mode.COMPOSE) {
					final int key = letterToKey(String.valueOf(e.getKeyChar())
							.toUpperCase());
					if (key < MAX_KEYS && key >= 0) {
						int firstSa = Key.notes.indexOf("Sa");
						String prefix = "";
						String suffix = "";
						if (key < firstSa)
							prefix = ".";
						else if (key > firstSa + 12)
							suffix = ".";
						shabadEditor
								.insert(prefix + Key.notes.get(key) + suffix
										+ " ", shabadEditor.getCaretPosition());
						new Thread(new Runnable() {
							public void run() {
								keys[key].playOnce(500);
							}
						}).start();
					}
				}
			}
		}
	}

	private int letterToKey(String letter) {
		int key = -20;

		if (letter.equals("A")) {
			key = 7;
		} else if (letter.equals("W")) {
			key = 8;
		} else if (letter.equals("S")) {
			key = 9;
		} else if (letter.equals("E")) {
			key = 10;
		} else if (letter.equals("D")) {
			key = 11;
		} else if (letter.equals("F")) {
			key = 12;
		} else if (letter.equals("T")) {
			key = 13;
		} else if (letter.equals("G")) {
			key = 14;
		} else if (letter.equals("Y")) {
			key = 15;
		} else if (letter.equals("H")) {
			key = 16;
		} else if (letter.equals("J")) {
			key = 17;
		} else if (letter.equals("I")) {
			key = 18;
		} else if (letter.equals("K")) {
			key = 19;
		} else if (letter.equals("O")) {
			key = 20;
		} else if (letter.equals("L")) {
			key = 21;
		} else if (letter.equals("P")) {
			key = 22;
		} else if (letter.equals(";")) {
			key = 23;
		} else if (letter.equals("'")) {
			key = 24;
		} else if (letter.equals("]")) {
			key = 25;
		} else if (letter.equals("Z")) {
			LOGGER.info("User pressed Z.");
			LOGGER.info("Initial Octave: " + octave);
			if (octave == Octave.UPPER)
				octave = Octave.MIDDLE;
			else if (octave == Octave.MIDDLE)
				octave = Octave.LOWER;
			LOGGER.info("Final Octave: " + octave);

			for (Key k : keys) {
				if (keyLabeled)
					k.labelKeyboardNote(octave);
			}
		} else if (letter.equals("X")) {
			LOGGER.info("User pressed X.");
			LOGGER.info("Initial Octave: " + octave);
			if (octave == Octave.MIDDLE)
				octave = Octave.UPPER;
			else if (octave == Octave.LOWER)
				octave = Octave.MIDDLE;
			LOGGER.info("Final Octave: " + octave);

			for (Key k : keys) {
				if (keyLabeled)
					k.labelKeyboardNote(octave);
			}
		}

		if (octave == Octave.LOWER)
			key -= 12;
		else if (octave == Octave.UPPER)
			key += 12;
		return key;
	}

	class HelpMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			LOGGER.info("Action Performed: " + command);
			if (command.equals("help")) {
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
	}

	class CheckBoxListener implements ItemListener {
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

	class UndoAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.undo();
			} catch (CannotUndoException ex) {
				LOGGER.warning("Unable to undo: " + ex);
				ex.printStackTrace();
			}
			updateUndoState();
			redoAction.updateRedoState();
		}

		protected void updateUndoState() {
			if (undo.canUndo()) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}

	class RedoAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public RedoAction() {
			super("Redo");

			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.redo();
			} catch (CannotRedoException ex) {
				LOGGER.warning("Unable to redo: " + ex);
				ex.printStackTrace();
			}
			updateRedoState();
			undoAction.updateUndoState();
		}

		protected void updateRedoState() {
			if (undo.canRedo()) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}

	class ShabadEditorListener implements DocumentListener,
			UndoableEditListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			// Empty for plain text components
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			updateTitleOnChange();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			updateTitleOnChange();
		}

		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
			// Remember the edit and update the menus
			undo.addEdit(e.getEdit());
			undoAction.updateUndoState();
			redoAction.updateRedoState();

			updateTitleOnChange();
		}

		private void updateTitleOnChange() {
			String title = frame.getTitle();

			if (!undo.canUndo()) {
				if (title.contains("*")) {
					frame.setTitle(frame.getTitle().substring(0,
							frame.getTitle().length() - 1));
				}
			} else {
				if (!title.contains("*"))
					frame.setTitle(frame.getTitle() + "*");
			}
		}
	}

	@SuppressWarnings("serial")
	class KeyAction extends AbstractAction {
		private SpaceKey key;

		public KeyAction(JTextArea textArea, SpaceKey key) {
			this.key = key;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (key.equals(SpaceKey.SPACE) && mode == Mode.COMPOSE) {
				shabadEditor.insert(" ", shabadEditor.getCaretPosition());
			} else if (key.equals(SpaceKey.BACKSPACE)) {
				int pos = shabadEditor.getCaretPosition();
				try {
					int start = shabadEditor.getSelectionStart();
					int end = shabadEditor.getSelectionEnd();
					if (start != end) {
						shabadEditor.getDocument().remove(start, end - start);
					}

					if (pos != 0 && start == end) {
						shabadEditor.getDocument().remove(pos - 1, 1);
					}
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
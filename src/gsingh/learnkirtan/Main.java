package gsingh.learnkirtan;

import static gsingh.learnkirtan.Constants.VERSION;
import static gsingh.learnkirtan.Constants.WHITE_KEY_HEIGHT;
import static gsingh.learnkirtan.Constants.WHITE_KEY_WIDTH;
import gsingh.learnkirtan.FileManager.SaveResult;
import gsingh.learnkirtan.component.ControlPanel;
import gsingh.learnkirtan.component.PianoPanel;
import gsingh.learnkirtan.component.View;
import gsingh.learnkirtan.component.menu.MenuBar;
import gsingh.learnkirtan.component.shabadeditor.SwingShabadEditor;
import gsingh.learnkirtan.component.shabadeditor.TableShabadEditor;
import gsingh.learnkirtan.controller.ControllerFactory;
import gsingh.learnkirtan.installer.SoundBankInstaller;
import gsingh.learnkirtan.installer.SoundBankInstallerFactory;
import gsingh.learnkirtan.keys.KeyMapper;
import gsingh.learnkirtan.keys.LabelManager;
import gsingh.learnkirtan.note.NoteList;
import gsingh.learnkirtan.player.ShabadPlayer;
import gsingh.learnkirtan.settings.SettingsManager;
import gsingh.learnkirtan.utility.NetworkUtility;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The main entry point for the program. Responsible for initializing the GUI,
 * installing the soundbank if necessary, and checking for updates
 * 
 * @author Gulshan
 * 
 */
public class Main {

	/** The base title of the software. Includes the name and version number */
	public static final String BASETITLE = "Learn Kirtan v" + VERSION
			+ " Beta - ";

	/** The width of the screen */
	private final int WIDTH = 3 * (WHITE_KEY_WIDTH * 7) + WHITE_KEY_WIDTH - 62;

	/**
	 * The main shabad editor. When play is pressed, the text in here will be
	 * played. It cannot be edited while playing.
	 */
	public SwingShabadEditor shabadEditor;

	/** The main frame */
	private JFrame frame;

	/** Manages changes in the title of the window */
	private WindowTitleManager titleManager;

	/** Handles the opening and saving of files */
	private FileManager fileManager = new FileManager();

	/** Manages the setting and getting of settings */
	private SettingsManager settingsManager = SettingsManager.getInstance();

	/** A list of all of the notes */
	private NoteList notes;

	/** Manages the labelling of the keys */
	private LabelManager labelManager;

	// Only Main can instantiate itself
	private Main() {
	}

	public static void main(String[] args) {
		new Main().init();
	}

	/**
	 * Constructs the GUI, installs the sound bank, and checks for updates
	 */
	public void init() {
		settingsManager.setFileManager(fileManager);
		notes = new NoteList(settingsManager.getSaKey());

		labelManager = new LabelManager(notes);
		KeyMapper.getInstance().setNotes(notes);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Initialize GUI
				createAndShowGui();

				// Install soundbank if necessary
				SoundBankInstaller installer = SoundBankInstallerFactory
						.getInstaller();
				installer.installSoundBank();

				// Check for update
				new Thread(new Runnable() {
					@Override
					public void run() {
						NetworkUtility.checkForUpdate();
					}
				}).start();
			}
		});
	}

	/**
	 * Constructs the menu, control panel, piano panel, and shabad editor
	 */
	public void createAndShowGui() {
		StateModel model = new StateModel();

		frame = new JFrame(BASETITLE + "Untitled Shabad");
		titleManager = new WindowTitleManager(frame);

		shabadEditor = new TableShabadEditor(titleManager);

		frame.setJMenuBar(new MenuBar(new ControllerFactory(fileManager, model,
				notes, shabadEditor, titleManager, labelManager), shabadEditor));

		JPanel controlPanel = new ControlPanel(new ShabadPlayer(model),
				shabadEditor);
		JComponent pianoPanel = new PianoPanel(labelManager);
		model.registerView((View) controlPanel);
		model.registerView((View) pianoPanel);

		JPanel mainPanel = initMainPanel(controlPanel, pianoPanel, shabadEditor);
		frame.add(mainPanel);

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new MyWindowAdapter());
		frame.pack();
		frame.setLocation(250, 60);
		frame.setResizable(false);
		frame.setVisible(true);

		shabadEditor.reset();
	}

	// TODO: A box layout would be fine here
	/**
	 * Constructs the mainPanel by taking the {@code controlPanel},
	 * {@code pianoPanel}, and {@code editor} and arranging them using a
	 * {@link GridBagLayout}.
	 * 
	 */
	private JPanel initMainPanel(JPanel controlPanel, JComponent pianoPanel,
			SwingShabadEditor editor) {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
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

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		editor.setPreferredSize(new Dimension(editor.getWidth(), 250));
		mainPanel.add(editor, c);

		return mainPanel;
	}

	private class MyWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent ev) {
			try {
				SaveResult result = null;
				if (shabadEditor.isModified()) {
					result = fileManager.safeSave(shabadEditor.getShabad());
				}
				if (result != SaveResult.NOTSAVEDCANCELLED) {
					System.exit(0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

package gsingh.learnkirtan;

import gsingh.learnkirtan.keys.BlackKey;
import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.WhiteKey;

import java.awt.Container;
import java.awt.GridLayout;
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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Main implements ActionListener, ItemListener {

	final int WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT, BLACK_KEY_WIDTH,
			BLACK_KEY_HEIGHT;
	final JFileChooser fc;
	{
		WHITE_KEY_WIDTH = Key.WHITE_KEY_WIDTH;
		BLACK_KEY_WIDTH = Key.BLACK_KEY_WIDTH;
		WHITE_KEY_HEIGHT = Key.WHITE_KEY_HEIGHT;
		BLACK_KEY_HEIGHT = Key.BLACK_KEY_HEIGHT;
		fc = new JFileChooser();
	}

	public static Key keys[] = new Key[24];
	private static int index = 0;

	JTextArea shabadEditor = null;
	JFrame frame;
	File curFile;

	public Main() {
		frame = new JFrame("Learn Kirtan");
		initMenu();

		JPanel mainPanel = new JPanel();
		JLayeredPane pianoPanel = new JLayeredPane();

		GridLayout layout = new GridLayout(2, 1);
		mainPanel.setLayout(layout);

		// Construct the shabad editor and the keyboard
		shabadEditor = new JTextArea();
		constructKeyboard(pianoPanel);

		// Add the piano panel and shabad editor to the window
		mainPanel.add(pianoPanel);
		mainPanel.add(shabadEditor);
		frame.add(mainPanel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(2 * (WHITE_KEY_WIDTH * 7 + WHITE_KEY_WIDTH / 2),
				WHITE_KEY_HEIGHT * 3);
		frame.setLocation(250, 100);
		frame.setVisible(true);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Parser.parseAndPlay();
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

		for (int k = 0; k < 2; k++) {
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
		if (command.equals("create")) {
			int result = askForSave();
			if (result != JOptionPane.CANCEL_OPTION) {
				if (result == JOptionPane.YES_OPTION)
					save();
				curFile = null;
				shabadEditor.setText("");
			}

		} else if (command.equals("open")) {
			int result = askForSave();
			if (result != JOptionPane.CANCEL_OPTION) {
				if (result == JOptionPane.YES_OPTION)
					save();
				int returnVal = fc.showOpenDialog(frame);

				BufferedReader br = null;
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					curFile = fc.getSelectedFile();
					try {
						br = new BufferedReader(new FileReader(curFile));
						shabadEditor.read(br, "File");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			}
		} else if (command.equals("showeditor")) {

		} else if (command.equals("save")) {
			save();
		}
	}

	public int askForSave() {
		return JOptionPane.showConfirmDialog(frame,
				"Would you like to save before proceeding?");
	}

	public void save() {
		BufferedWriter bw = null;
		if (curFile == null) {
			int returnVal = fc.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				curFile = fc.getSelectedFile();

				if (curFile.exists()) {
					int result = JOptionPane.showConfirmDialog(frame,
							"Overwrite existing file?", "Confirm Overwrite",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						try {
							bw = new BufferedWriter(new FileWriter(curFile));
							shabadEditor.write(bw);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
				}
			}
		} else {
			try {
				bw = new BufferedWriter(new FileWriter(curFile));
				shabadEditor.write(bw);
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

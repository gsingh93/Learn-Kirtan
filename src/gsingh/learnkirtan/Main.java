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

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Main implements ActionListener, ItemListener {

	final int WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT, BLACK_KEY_WIDTH,
			BLACK_KEY_HEIGHT;
	{
		WHITE_KEY_WIDTH = Key.WHITE_KEY_WIDTH;
		BLACK_KEY_WIDTH = Key.BLACK_KEY_WIDTH;
		WHITE_KEY_HEIGHT = Key.WHITE_KEY_HEIGHT;
		BLACK_KEY_HEIGHT = Key.BLACK_KEY_HEIGHT;
	}

	public static Key keys[] = new Key[24];
	private static int index = 0;

	JTextArea shabadEditor = null;
	JFrame frame;

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

		JMenuItem createItem = new JMenuItem("Create new shabad", KeyEvent.VK_C);
		JMenuItem openItem = new JMenuItem("Open existing shabad",
				KeyEvent.VK_O);
		createItem.setActionCommand("create");
		createItem.addActionListener(this);
		openItem.setActionCommand("open");
		openItem.addActionListener(this);

		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(createItem);
		fileMenu.add(openItem);

		JMenuItem showEditorItem = new JMenuItem("Show shabad editor",
				KeyEvent.VK_S);
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

		} else if (command.equals("open")) {

		} else if (command.equals("showeditor")) {

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

package gsingh.learnkirtan;

import gsingh.learnkirtan.keys.BlackKey;
import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.WhiteKey;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class Main {

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

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		JFrame frame = new JFrame("Learn Kirtan");

		JLayeredPane panel = new JLayeredPane();
		frame.add(panel);

		constructKeyboard(panel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(2 * (WHITE_KEY_WIDTH * 7 + WHITE_KEY_WIDTH / 2),
				WHITE_KEY_HEIGHT);
		frame.setLocation(250, 100);
		frame.setVisible(true);

		Parser.parseAndPlay();
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
}

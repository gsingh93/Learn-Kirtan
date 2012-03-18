package gsingh.learnkirtan;

import gsingh.learnkirtan.keys.BlackKey;
import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.WhiteKey;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class Main {

	public static final int WHITE_KEY_WIDTH = 40;
	public static final int BLACK_KEY_WIDTH = 20;
	public static final int WHITE_KEY_HEIGHT = 200;
	public static final int BLACK_KEY_HEIGHT = 120;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		JFrame frame = new JFrame("Learn Kirtan");

		JLayeredPane panel = new JLayeredPane();
		frame.add(panel);

		constructKeyboard(panel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WHITE_KEY_WIDTH * 7 + WHITE_KEY_WIDTH / 2,
				WHITE_KEY_HEIGHT);
		frame.setLocation(250, 100);
		frame.setVisible(true);
	}

	void constructKeyboard(Container panel) {
		Key b;
		int i = 0;

		b = new WhiteKey();
		b.setLocation(i++ * WHITE_KEY_WIDTH, 0);
		panel.add(b, 0, -1);

		b = new BlackKey();
		b.setLocation(WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2, 0);
		panel.add(b, 1, -1);

		b = new WhiteKey();
		b.setLocation(i++ * WHITE_KEY_WIDTH, 0);
		panel.add(b, 0, -1);

		b = new BlackKey();
		b.setLocation(WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 + WHITE_KEY_WIDTH,
				0);
		panel.add(b, 1, -1);

		b = new WhiteKey();
		b.setLocation(i++ * WHITE_KEY_WIDTH, 0);
		panel.add(b, 0, -1);

		b = new WhiteKey();
		b.setLocation(i++ * WHITE_KEY_WIDTH, 0);
		panel.add(b, 0, -1);

		b = new BlackKey();
		b.setLocation(WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 + 3
				* WHITE_KEY_WIDTH, 0);
		panel.add(b, 1, -1);

		b = new WhiteKey();
		b.setLocation(i++ * WHITE_KEY_WIDTH, 0);
		panel.add(b, 0, -1);

		b = new BlackKey();
		b.setLocation(WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 + 4
				* WHITE_KEY_WIDTH, 0);
		panel.add(b, 1, -1);

		b = new WhiteKey();
		b.setLocation(i++ * WHITE_KEY_WIDTH, 0);
		panel.add(b, 0, -1);

		b = new BlackKey();
		b.setLocation(WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 + 5
				* WHITE_KEY_WIDTH, 0);
		panel.add(b, 1, -1);

		b = new WhiteKey();
		b.setLocation(i++ * WHITE_KEY_WIDTH, 0);
		panel.add(b, 0, -1);
	}
}

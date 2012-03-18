package gsingh.learnkirtan.keys;

import java.awt.Color;

public class WhiteKey extends Key {

	public static final int WHITE_KEY_WIDTH = 40;
	public static final int WHITE_KEY_HEIGHT = 200;

	private static final long serialVersionUID = 1L;

	public WhiteKey() {
		super();
		setBackground(Color.WHITE);
		setSize(WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
	}

}

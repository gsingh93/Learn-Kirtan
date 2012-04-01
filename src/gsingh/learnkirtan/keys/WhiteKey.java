package gsingh.learnkirtan.keys;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

public class WhiteKey extends Key {

	private static final long serialVersionUID = 1L;

	/**
	 * Font to use for note labels
	 */
	private static Font font = new Font("Dialog", Font.PLAIN, 14);

	public WhiteKey() {
		super();
		setMargin(new Insets(100, 0, 0, 0));
		setFont(font);
		setBackground(Color.WHITE);
		setSize(WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
	}

}

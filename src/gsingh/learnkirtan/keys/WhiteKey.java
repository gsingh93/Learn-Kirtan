package gsingh.learnkirtan.keys;

import static gsingh.learnkirtan.Constants.WHITE_KEY_HEIGHT;
import static gsingh.learnkirtan.Constants.WHITE_KEY_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

@SuppressWarnings("serial")
public class WhiteKey extends Key {

	/**
	 * Font to use for note labels
	 */
	private static Font font = new Font("Dialog", Font.PLAIN, 14);

	public WhiteKey(LabelManager labelManager) {
		super(labelManager);
		setMargin(new Insets(100, 0, 0, 0));
		setFont(font);
		setBackground(Color.WHITE);
		setSize(WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
	}
}

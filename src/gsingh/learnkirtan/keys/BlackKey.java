package gsingh.learnkirtan.keys;

import static gsingh.learnkirtan.Constants.BLACK_KEY_HEIGHT;
import static gsingh.learnkirtan.Constants.BLACK_KEY_WIDTH;

import java.awt.Color;
import java.awt.Insets;

@SuppressWarnings("serial")
public class BlackKey extends Key {

	public BlackKey(LabelManager labelManager) {
		super(labelManager);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setMargin(new Insets(0, 0, 0, 0));
		setSize(BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
	}
}

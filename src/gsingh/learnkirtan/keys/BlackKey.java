package gsingh.learnkirtan.keys;

import java.awt.Color;
import java.awt.event.MouseListener;

public class BlackKey extends Key implements MouseListener {

	private static final long serialVersionUID = 1L;

	public BlackKey() {
		super();
		setBackground(Color.BLACK);
		setSize(BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
	}
}

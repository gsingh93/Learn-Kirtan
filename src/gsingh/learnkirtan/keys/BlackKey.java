package gsingh.learnkirtan.keys;

import java.awt.Color;
import java.awt.event.MouseListener;

public class BlackKey extends Key implements MouseListener {

	private static final long serialVersionUID = 1L;

	public static final int BLACK_KEY_WIDTH = 20;
	public static final int BLACK_KEY_HEIGHT = 120;

	public BlackKey() {
		super();
		setBackground(Color.BLACK);
		setSize(BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
	}
}

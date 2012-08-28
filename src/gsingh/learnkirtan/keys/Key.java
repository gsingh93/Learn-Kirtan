package gsingh.learnkirtan.keys;

import static gsingh.learnkirtan.keys.LabelManager.EMPTY_KEY_SPAN_TAG;
import static gsingh.learnkirtan.keys.LabelManager.EMPTY_SARGAM_SPAN_TAG;
import gsingh.learnkirtan.Constants;
import gsingh.learnkirtan.utility.Utility;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Key extends JButton implements MouseListener {

	/** The default label for this key */
	private static final String DEFAULT_TEXT = String.format(
			"<html><div style='text-align:center'>%s<br>%s</div></html>",
			EMPTY_SARGAM_SPAN_TAG, EMPTY_KEY_SPAN_TAG);

	/**
	 * A counter used to assign MIDI note numbers to the keys
	 */
	private static int noteCount = Constants.STARTING_MIDI_NOTE_ID;

	/**
	 * The MIDI note ID of the key
	 */
	private final int midiNoteId;

	/** The pressed state of this key */
	private boolean pressed;

	/** The clicked state of this key */
	private boolean clicked;

	/** The MidiPlayer responsible for playing note corresponding to this key */
	private MidiPlayer midiPlayer;

	public Key(LabelManager labelManager) {

		this.midiNoteId = noteCount++;
		setText(DEFAULT_TEXT);
		addKeyListener(new KeyboardListener(labelManager));
		addMouseListener(this);

		// TODO: Move to shabadPlayer?
		midiPlayer = new MidiPlayer(midiNoteId);
	}

	/**
	 * Checks if the key label contains the provided text
	 * 
	 * @param text
	 *            the text to search for
	 * @return true if the text is found, false otherwise
	 */
	public boolean contains(String text) {
		return getText().contains(text);
	}

	/**
	 * Replaces a literal string in the label
	 * 
	 * @param target
	 *            the text to be replaced
	 * @param replacement
	 *            the text to replace with
	 */
	public void replaceText(String target, String replacement) {
		setText(getText().replace(target, replacement));
	}

	/**
	 * Replaces all text that matches a regex expression
	 * 
	 * @param regex
	 *            the regex expression to replace
	 * @param replacement
	 *            the text to replace with
	 */
	public void replaceAll(String regex, String replacement) {
		setText(getText().replaceAll(regex, replacement));
	}

	/** @return the midiNoteId of this key */
	public int getMIDINoteId() {
		return midiNoteId;
	}

	/** @return true if this key is pressed, false otherwise */
	public boolean isPressed() {
		return pressed;
	}

	/**
	 * A wrapper for the {@code doClick} method, so that it is executed if the
	 * key is pressed or clicked
	 */
	public void startDoClick() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (pressed || clicked) {
					doClick(200);
				}
			}
		}).start();
	}

	/**
	 * Plays this key one time for the specified amount of time
	 * 
	 * @param time
	 *            the amount of time to hold this key down
	 */
	public void playOnce(final int time) {
		midiPlayer.play();
		doClick(time);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		midiPlayer.stop();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Key clicked: " + midiNoteId);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// e will be null if called this function ourselves when a key was
		// pressed
		if (e == null) {
			pressed = true;
		} else {
			clicked = true;
		}
		midiPlayer.play();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// e will be null if called this function ourselves when a key was
		// released
		if (e == null) {
			pressed = false;
		} else {
			clicked = false;
		}

		// If the note is neither pressed or clicked, stop playing it
		// If one of the two is true, then stop playing it and restart,
		// so that only one note is played at a time
		if (!pressed && !clicked)
			midiPlayer.stop();
		else {
			// TODO: Should MidiPlayer handle if the note is already being
			// played?
			midiPlayer.stop();
			midiPlayer.play();
		}
	}

	private class KeyboardListener extends KeyAdapter {

		private LabelManager labelManager;

		public KeyboardListener(LabelManager labelManager) {
			this.labelManager = labelManager;
		}

		// TODO: Look into an alternative for each key being responsible for
		// every other key. If a fix is not possible, don't worry about it.
		/**
		 * Play the key that was pressed if it is playable. Note that this does
		 * not have to be this key; it can be any playable key.
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			// Get the keyId of the key that was pressed
			final int keyId = Utility.letterToKeyId(
					String.valueOf(e.getKeyChar()), labelManager.getOctave());

			// If that key is playable and not already pressed, play it
			if (keyId < Constants.MAX_KEYS && keyId >= 0) {
				final Key key = KeyMapper.getInstance().getKeys()[keyId];
				if (!key.isPressed()) {
					key.mousePressed(null);
					key.startDoClick();
				}
			}
		}

		/**
		 * Unpress the key or shift the octave depending on which key was
		 * released
		 */
		@Override
		public void keyReleased(KeyEvent e) {
			String letter = String.valueOf(e.getKeyChar()).toUpperCase();

			if (letter.equals("Z")) {
				labelManager.shiftOctaveDown();
				labelManager.labelKeyboardNotes();
			} else if (letter.equals("X")) {
				labelManager.shiftOctaveUp();
				labelManager.labelKeyboardNotes();
			} else {
				int keyId = Utility.letterToKeyId(letter,
						labelManager.getOctave());
				if (keyId < Constants.MAX_KEYS && keyId >= 0) {
					Key key = KeyMapper.getInstance().getKeys()[keyId];
					key.mouseReleased(null);
				}
			}
		}
	}
}

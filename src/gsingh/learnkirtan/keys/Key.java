package gsingh.learnkirtan.keys;

import gsingh.learnkirtan.Main;
import gsingh.learnkirtan.Parser;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JButton;

public class Key extends JButton implements MouseListener {

	private static final Logger LOGGER = Logger.getLogger(Parser.class
			.getName());

	private static final long serialVersionUID = 1L;

	public static final int WHITE_KEY_HEIGHT = 200;
	public static final int WHITE_KEY_WIDTH = 40;
	public static final int BLACK_KEY_WIDTH = 20;
	public static final int BLACK_KEY_HEIGHT = 120;

	/**
	 * A counter used to assign midi note numbers to the keys
	 */
	private static int noteCount = 40;

	/**
	 * The midi note number of the key
	 */
	public int note;

	/**
	 * The note names for all the notes on the keyboard
	 */
	public static List<String> notes = new LinkedList<String>(
			Arrays.asList(new String[] { "Re", "'Ga", "Ga", "Ma", "Ma'", "Pa",
					"'Dha", "Dha", "'Ni", "Ni", "Sa", "'Re", "Re", "'Ga", "Ga",
					"Ma", "Ma'", "Pa", "'Dha", "Dha", "'Ni", "Ni", "Sa", "'Re",
					"Re", "'Ga", "Ga", "Ma", "Ma'", "Pa", "'Dha", "Dha", "'Ni",
					"Ni", "Sa", "'Re" }));

	private static Synthesizer synth = null;

	/**
	 * Allows for a one time shift of keys based on user settings at startup
	 */
	private static boolean shifted = false;

	static {
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	MidiChannel channel[];

	public Key(int saKey) {
		LOGGER.addHandler(Main.logFile);
		LOGGER.setLevel(Level.INFO);

		note = noteCount++;
		shiftKeys(saKey);
		label();
		channel = synth.getChannels();
		addKeyListener(new KeyboardListener());

		// Sets the instrument to an instrument close to a harmonium
		channel[0].programChange(20);
		addMouseListener(this);

	}

	public static void shiftKeys(int saKey) {

		if (!shifted) {
			int difference = saKey - 10;

			if (difference > 0) {
				for (int i = 0; i < difference; i++) {
					Key.notes.add(0, Key.notes.remove((Key.notes.size() - 1)));
				}
			} else if (difference < 0) {
				for (int i = 0; i < -1 * difference; i++) {
					Key.notes.add(Key.notes.size() - 1, Key.notes.remove(0));
				}
			}
			shifted = true;
		}
	}

	public void label() {
		setText(notes.get(note - 40));
	}

	/**
	 * Plays the midi note of this key for the specified amount of time
	 * 
	 * @param time
	 *            - the length of time to play the note in milliseconds
	 */
	public void playOnce(int time) {
		play();
		doClick(time);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stop();
	}

	/**
	 * Starts playing the midi note for this key
	 */
	public void play() {
		channel[0].noteOn(note, 60);
	}

	/**
	 * Stops playing the midi note of this key
	 */
	public void stop() {
		channel[0].noteOff(note);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		LOGGER.info("Key clicked: " + note);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		play();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		stop();
	}

	class KeyboardListener extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {
			Main.getMain().notePressed(e);
		}
	}
}

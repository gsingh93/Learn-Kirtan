package gsingh.learnkirtan.keys;

import gsingh.learnkirtan.Constants.Octave;
import gsingh.learnkirtan.Main;
import gsingh.learnkirtan.SettingsManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Key extends JButton implements MouseListener {

	private static final Logger LOGGER = Logger.getLogger(Key.class.getName());
	static {
		LOGGER.addHandler(Main.logFile);
		LOGGER.setLevel(Level.INFO);
	}

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

	private static List<String> keys = new LinkedList<String>(
			Arrays.asList(new String[] { "A", "W", "S", "E", "D", "F", "T",
					"G", "Y", "H", "J", "I", "K", "O", "L", "P", ";", "'", "]" }));
	private static HashMap<Integer, String> keyMapLower = new HashMap<Integer, String>();
	private static HashMap<Integer, String> keyMapMiddle = new HashMap<Integer, String>();
	private static HashMap<Integer, String> keyMapUpper = new HashMap<Integer, String>();

	private boolean pressed = false;

	static {
		for (int i = 0; i < 13; i++) {
			keyMapLower.put(40 + i, keys.get(i + 5));
		}
		for (int i = 0; i < 19; i++) {
			keyMapMiddle.put(47 + i, keys.get(i));
		}
		for (int i = 0; i < 15; i++) {
			keyMapUpper.put(59 + i, keys.get(i));
		}
	}

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

	public Key(int saKey, SettingsManager sm) {
		LOGGER.addHandler(Main.logFile);
		LOGGER.setLevel(Level.INFO);

		note = noteCount++;
		shiftKeys(saKey);
		setText("<html><div style='text-align:center'><span id='sargam'></span><br><span id='key'></span></div></html>");
		if (sm.getShowSargamLabels())
			labelSargamNote();
		if (sm.getShowKeyboardLabels())
			labelKeyboardNote(Octave.MIDDLE);
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

	public void labelSargamNote() {
		clearSargamNote();
		setText(getText().replace("<span id='sargam'>",
				"<span id='sargam'>" + notes.get(note - 40)));
	}

	public void labelKeyboardNote(Octave octave) {
		clearKeyboardNote(octave);
		if (octave == Octave.LOWER) {
			if (keyMapLower.containsKey(note))
				setText(getText().replace("<span id='key'>",
						"<span id='key'>" + keyMapLower.get(note)));
		} else if (octave == Octave.MIDDLE) {
			if (keyMapMiddle.containsKey(note))
				setText(getText().replace("<span id='key'>",
						"<span id='key'>" + keyMapMiddle.get(note)));
		} else if (octave == Octave.UPPER) {
			if (keyMapUpper.containsKey(note))
				setText(getText().replace("<span id='key'>",
						"<span id='key'>" + keyMapUpper.get(note)));
		}
	}

	public void clearSargamNote() {
		setText(getText().replaceAll("<span id='sargam'>[A-Za-z'.]*</span>",
				"<span id='sargam'></span>"));
	}

	public void clearKeyboardNote(Octave octave) {
		setText(getText().replaceAll("<span id='key'>[A-Z;'\\]]*</span>",
				"<span id='key'></span>"));
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

	public boolean isPressed() {
		return pressed;
	}

	public void startDoClick() {
		while (pressed) {
			doClick(50);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		LOGGER.info("Key clicked: " + note);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
		play();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = false;
		stop();
	}

	class KeyboardListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			Main.getMain().notePressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Main.getMain().noteReleased(e);
		}
	}
}

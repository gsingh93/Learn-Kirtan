package gsingh.learnkirtan.player;

import gsingh.learnkirtan.keys.Key;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

/**
 * A facade for the Java MIDI API. Provides an interface for playing the MIDI
 * note associated with a {@link Key}
 * 
 * @author Gulshan
 * 
 */
public class MidiPlayer {
	/** The channel used to find the harmonium MIDI sound */
	private static final int CHANNEL = 0;

	/** The program used to find the harmonium MIDI sound */
	private static final int PROGRAM = 20;

	/** The {@link Synthesizer} channel that will contain the harmonium sound */
	private static final MidiChannel channel;

	static {
		Synthesizer synth = null;
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}

		channel = synth.getChannels()[CHANNEL];

		// Sets the instrument to an instrument close to a harmonium
		channel.programChange(PROGRAM);
	}

	private MidiPlayer() {
	}

	/**
	 * Plays a {@link Key} for the specified length of time
	 * 
	 * @param key
	 *            the {@link Key} to play
	 * @param length
	 *            the length of time in milliseconds to play the key
	 */
	public static void play(Key key, int length) {
		play(key.getMidiNoteId());
		key.doClick(length);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stop(key.getMidiNoteId());
	}

	/**
	 * Starts playing the MIDI note for this key
	 * 
	 * @param note
	 *            the MIDI note ID of the note to play
	 */
	public static void play(int note) {
		channel.noteOn(note, 60);
	}

	/**
	 * Stops playing the MIDI note of this key
	 * 
	 * @param note
	 *            the MIDI note ID of the note to play
	 */
	public static void stop(int note) {
		channel.noteOff(note);
	}
}

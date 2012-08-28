package gsingh.learnkirtan.keys;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class MidiPlayer {
	private static Synthesizer synth = null;
	static {
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	MidiChannel channel[];
	private int note;

	public MidiPlayer(int note) {
		channel = synth.getChannels();

		// Sets the instrument to an instrument close to a harmonium
		channel[0].programChange(20);

		this.note = note;
	}

	public void playOnce(int time) {
		play();
		try {
			Thread.sleep(time);
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
}

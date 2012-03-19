package gsingh.learnkirtan.keys;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JButton;

public class Key extends JButton implements MouseListener {

	private static final long serialVersionUID = 1L;

	public static final int WHITE_KEY_HEIGHT = 200;
	public static final int WHITE_KEY_WIDTH = 40;
	public static final int BLACK_KEY_WIDTH = 20;
	public static final int BLACK_KEY_HEIGHT = 120;

	private static int noteCount = 40;
	public int note;

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

	public Key() {
		note = noteCount++;

		// Instrument[] instruments = synth.getAvailableInstruments();
		// for (Instrument instrument : instruments) {
		// System.out.println(instrument.getName());
		// System.out.println(instrument.getPatch().getBank());
		// System.out.println(instrument.getPatch().getProgram());
		// }

		channel = synth.getChannels();
		channel[0].programChange(20);
		addMouseListener(this);
	}

	public void playOnce() {
		play();
		doClick(1000);
		stop();
	}

	public void play() {
		channel[0].noteOn(note, 60);
	}

	public void stop() {
		channel[0].noteOff(note);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(this.note);
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

}

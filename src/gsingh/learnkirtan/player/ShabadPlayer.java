package gsingh.learnkirtan.player;

import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.KeyMapper;
import gsingh.learnkirtan.listener.PlayEventListener;
import gsingh.learnkirtan.listener.PlayEventListener.PlayEvent;
import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.note.Note.Length;
import gsingh.learnkirtan.parser.exceptions.NoteOutOfBoundsException;
import gsingh.learnkirtan.shabad.Shabad;
import gsingh.learnkirtan.shabad.ShabadNotes;
import gsingh.learnkirtan.utility.DialogUtility;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShabadPlayer {

	private boolean pause;
	private boolean stop = false;

	private static final Map<Length, Integer> noteLengths;
	static {
		Map<Length, Integer> temp = new HashMap<Length, Integer>();
		temp.put(Length.HALF, 500);
		temp.put(Length.FULL, 1000);
		temp.put(Length.LONG, 2000);
		noteLengths = Collections.unmodifiableMap(temp);
	}
	
	private List<PlayEventListener> listeners = new LinkedList<PlayEventListener>();

	public void play(Shabad shabad, double tempo, boolean repeat) {
		for (PlayEventListener l : listeners) {
			l.onPlayEvent(PlayEvent.PLAY);
		}

		boolean finished = false;
		ShabadNotes notes = shabad.getNotes();
		do {
			if (notes.empty()) {
				DialogUtility.nothingSelected();
				break;
			}
			for (Note note : notes) {
				if (note != null) {
					while (pause) {
						if (stop) {
							finished = true;
							break;
						}
						sleep(500);
					}
					if (stop) {
						finished = true;
						stop = false;
						pause = false;
						break;
					}
					try {
						playNote(note, tempo);
					} catch (NoteOutOfBoundsException e) {
						stop = true;
						DialogUtility.showNoteOutOfBoundsDialog(e.getNote());
					}
				}
			}
			if (finished) {
				break;
			}
		} while (repeat);

		for (PlayEventListener l : listeners) {
			l.onPlayEvent(PlayEvent.STOP);
		}
	}

	public static void playNote(Note note, double tempo)
			throws NoteOutOfBoundsException {
		Key key = KeyMapper.getInstance().getKeyFromNote(note);
		int length = (int) (noteLengths.get(note.getLength()) / tempo);
		MidiPlayer.play(key, length);
	}

	public void addPlayEventListener(PlayEventListener l) {
		listeners.add(l);
	}

	public void pause() {
		pause = true;

		for (PlayEventListener l : listeners) {
			l.onPlayEvent(PlayEvent.PAUSE);
		}
	}

	public void unpause() {
		pause = false;

		for (PlayEventListener l : listeners) {
			l.onPlayEvent(PlayEvent.PLAY);
		}
	}

	public boolean isPaused() {
		return pause;
	}

	public void stop() {
		stop = true;
		for (PlayEventListener l : listeners) {
			l.onPlayEvent(PlayEvent.STOP);
		}
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

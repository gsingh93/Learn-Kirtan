package gsingh.learnkirtan.player;

import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.KeyMapper;
import gsingh.learnkirtan.listener.PlayEventListener;
import gsingh.learnkirtan.listener.PlayEventListener.PlayEvent;
import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.shabad.Shabad;
import gsingh.learnkirtan.shabad.ShabadNotes;

import java.util.LinkedList;
import java.util.List;

public class ShabadPlayer {

	private boolean pause;
	private boolean stop = false;
	
	private List<PlayEventListener> listeners = new LinkedList<PlayEventListener>();

	public void play(Shabad shabad, double tempo, boolean repeat) {
		for (PlayEventListener l : listeners) {
			l.onPlayEvent(PlayEvent.PLAY);
		}

		boolean finished = false;
		ShabadNotes notes = shabad.getNotes();
		do {
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
					playNote(note, tempo);
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

	public static void playNote(Note note, double tempo) {
		Key key = KeyMapper.getInstance().getKeyFromNote(note);
		MidiPlayer.play(key, (int) (note.getLength() / tempo));
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

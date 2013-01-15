package gsingh.learnkirtan.player;

import gsingh.learnkirtan.StateModel;
import gsingh.learnkirtan.StateModel.PlayState;
import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.KeyMapper;
import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.shabad.Shabad;

public class ShabadPlayer {

	private boolean pause;
	private boolean stop = false;
	private StateModel model;

	public ShabadPlayer(StateModel model) {
		this.model = model;
	}

	public void play(Shabad shabad, double tempo) {
		model.setPlayState(PlayState.PLAY);
		for (Note note : shabad.getNotes()) {
			if (note != null) {
				while (pause) {
					if (stop) {
						break;
					}
					sleep(500);
				}
				if (stop) {
					stop = false;
					pause = false;
					break;
				}
				playNote(note, tempo);
			}
		}
		model.setPlayState(PlayState.STOP);
	}

	public void playNote(Note note, double tempo) {
		Key key = KeyMapper.getInstance().getKeyFromNote(note);
		MidiPlayer.play(key, (int) (note.getLength() / tempo));
	}

	public void pause() {
		model.setPlayState(PlayState.PAUSE);
		pause = true;
	}

	public void unpause() {
		model.setPlayState(PlayState.PLAY);
		pause = false;
	}

	public boolean isPaused() {
		return pause;
	}

	public void stop() {
		model.setPlayState(PlayState.STOP);
		stop = true;
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

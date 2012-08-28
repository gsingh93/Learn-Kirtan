package gsingh.learnkirtan.controller;

import gsingh.learnkirtan.StateModel;
import gsingh.learnkirtan.StateModel.PlayState;
import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.shabad.Shabad;

public class ShabadPlayer {

	private boolean pause;
	private StateModel model;

	public ShabadPlayer(StateModel model) {
		this.model = model;
	}

	public void play(Shabad shabad) {
		model.setPlayState(PlayState.PLAY);
		for (Note note : shabad.getNotes()) {
			while (pause) {
				sleep(500);
			}
			note.play();
		}
		model.setPlayState(PlayState.STOP);
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
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

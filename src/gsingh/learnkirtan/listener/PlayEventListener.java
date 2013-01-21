package gsingh.learnkirtan.listener;

public interface PlayEventListener {
	public enum PlayEvent {
		PLAY, PAUSE, STOP
	}

	public void onPlayEvent(PlayEvent e);
}
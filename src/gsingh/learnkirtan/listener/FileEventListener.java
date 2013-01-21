package gsingh.learnkirtan.listener;

public interface FileEventListener {
	public enum FileEvent {
		OPEN, SAVE, CREATE
	}

	public void onFileEvent(FileEvent e);
}
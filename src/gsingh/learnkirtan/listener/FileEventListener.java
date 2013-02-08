package gsingh.learnkirtan.listener;

public interface FileEventListener {
	public enum FileEvent {
		OPEN, SAVE, CREATE;

		private String filename;

		public void setFileName(String filename) {
			this.filename = filename;
		}

		public String getFileName() {
			return filename;
		}

	}

	public void onFileEvent(FileEvent e);
}
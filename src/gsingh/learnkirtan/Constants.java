package gsingh.learnkirtan;

// TODO Move these to correct location
public final class Constants {
	private Constants() {
	}

	/**
	 * Maximum number of keys
	 */
	public static final int MAX_KEYS = 34;

	public static enum Duration {
		DAY, WEEK, MONTH
	};

	public static final int WHITE_KEY_HEIGHT = 200;
	public static final int WHITE_KEY_WIDTH = 40;
	public static final int BLACK_KEY_WIDTH = 20;
	public static final int BLACK_KEY_HEIGHT = 120;

	public static final String VERSION = "0.5";
	public static final int STARTING_MIDI_NOTE_ID = 40;

}

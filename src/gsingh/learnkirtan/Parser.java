package gsingh.learnkirtan;

import gsingh.learnkirtan.note.DoubleNote;
import gsingh.learnkirtan.note.Note;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class Parser {

	private static final Logger LOGGER = Logger.getLogger(Parser.class
			.getName());

	static {
		LOGGER.addHandler(Main.logFile);
		LOGGER.setLevel(Level.INFO);
	}

	/**
	 * The default length each note is played
	 */
	private static final int gap = 500;

	/**
	 * If set, loop will terminate. Also used for immediate stops, such as
	 * reaching a ending label
	 */
	private static boolean stop = false;

	/**
	 * If set, playback will pause
	 */
	private static boolean pause = false;

	/**
	 * True if playing, false otherwise.
	 */
	private static boolean isPlaying = false;

	/**
	 * If set, end of file or playback region has been reached. If
	 * {@code repeat} is set, playback will repeat. Otherwise, this function
	 * will return
	 */
	private static boolean finished = false;

	/**
	 * If set, playback region will repeat when finished
	 */
	private static boolean repeat = false;

	/**
	 * The note to play
	 */
	private static DoubleNote doubleNote;

	/**
	 * An array list of shabad
	 */
	private static List<String> wordList;

	/**
	 * The index in {@code wordList} from which to get notes
	 */
	private static int index = 0;

	public static void parseAndPlay(String shabad, String start, String end,
			double tempo, int saKey) {

		// TODO: Add Exception class
		shabad = shabad.toUpperCase();
		LOGGER.info(shabad);
		wordList = Arrays.asList(shabad.split("\\s+"));

		// Find starting label if present
		start = start.toUpperCase();
		end = end.toUpperCase();
		if (!moveCursorToStart(start))
			stop = true;

		while (!stop) {
			if (pause)
				pause();

			// Read in note
			getNextNote(end);

			// Check for immediate end
			if (stop) {
				if (finish(start)) {
					stop = false;
					continue;
				} else
					break;
			}

			// Play notes if valid
			if (!playNote(doubleNote.getNote1(), tempo, saKey)
					|| !playNote(doubleNote.getNote2(), tempo, saKey))
				break;

			// Check for non-immediate end
			if (finished) {
				if (finish(start)) {
					finished = false;
					continue;
				} else
					break;
			}

		}

		LOGGER.info("Left Loop. Returning.");
		index = 0;
		pause = false;
		isPlaying = false;
		stop = false;
		finished = false;
	}

	private static boolean moveCursorToStart(String start) {
		String input = null;
		if (!start.equals("")) {
			input = wordList.get(index++);
			while (!input.equals("#" + start)) {
				if (index < wordList.size()) {
					input = wordList.get(index++);
				} else {
					LOGGER.warning("No starting label was found. Stopping playback.");
					stop = true;
					JOptionPane
							.showMessageDialog(
									null,
									"Error: You specified that playback should start at a label, "
											+ "but that label could not be found. Make sure there is a "
											+ "'#' before the label.", "Error",
									JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		} else
			LOGGER.info("No starting label supplied.");
		System.out.println(index);
		System.out.println(input);
		if (index < wordList.size())
			input = wordList.get(index);

		System.out.println(input);
		if (input.equals("-")) {
			LOGGER.warning("ERROR: Dash found as first note.");
			JOptionPane
					.showMessageDialog(
							null,
							"Error: The first note found was a dash, which is not allowed.",
							"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;

	}

	// TODO: Refactor
	private static void getNextNote(String end) {
		int maxLen = wordList.size();

		String input;
		if (index < maxLen) {
			input = wordList.get(index++);
			LOGGER.info("Input: " + input);
		} else {
			LOGGER.info("Reached End");
			finished = true;
			return;
		}

		// Check if label
		while (input.charAt(0) == '#' || input.equals("-")) {
			LOGGER.info("Checking Label: " + input);
			// Check if end label

			if (index >= maxLen) {
				LOGGER.info("Reached End");
				finished = true;
				return;
			}

			if (input.equals("#" + end)) {
				LOGGER.info("Reached End Label");
				stop = true;
				return;
			}

			input = wordList.get(index++);
			LOGGER.info("Input: " + input);
		}

		// Construct a note
		doubleNote = new DoubleNote(input);

		if (index < maxLen) {
			input = wordList.get(index++);
			LOGGER.info("Input: " + input);
		} else {
			LOGGER.info("Reached End");
			finished = true;
			return;
		}

		int holdCount = 1;
		while (input.equals("-")) {
			holdCount++;
			if (index < maxLen) {
				input = wordList.get(index++);
				LOGGER.info("Input: " + input);

				// Check if label
				while (input.charAt(0) == '#') {
					LOGGER.info("Checking Label: " + input);
					// Check if end label
					if (index >= maxLen) {
						LOGGER.info("Reached End");
						finished = true;
						break;
					}

					if (input.equals("#" + end)) {
						LOGGER.info("Reached End Label");
						finished = true;
						break;
					}

					input = wordList.get(index++);
					LOGGER.info("Input: " + input);
				}
			} else {
				LOGGER.info("Reached End");
				finished = true;
				break;
			}
		}

		index--;
		if (doubleNote.getNote2() == null)
			doubleNote.getNote1().setHoldCount(holdCount);
		else
			doubleNote.getNote2().setHoldCount(holdCount);

		LOGGER.info("holdCount: " + holdCount);
	}

	private static boolean playNote(Note note, double tempo, int saKey) {
		if (note != null) {
			LOGGER.info("Note is null.");
			if (note.isValid()) {
				play(note, tempo, saKey);
			} else {
				String fullNote = note.getPrefix() + note.getNote()
						+ note.getSuffix();
				LOGGER.warning("Invalid note: " + fullNote);
				JOptionPane.showMessageDialog(null, "Error: Invalid note '"
						+ fullNote + "'", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		return true;
	}

	public static void play(Note note, double tempo, int saKey) {
		// Calculate key for note
		int key = calculateKey(note, saKey);

		// Play note
		double doubleMult;
		if (doubleNote.getNote2() != null)
			doubleMult = .5;
		else
			doubleMult = 1.0;
		LOGGER.info("Playing Key: " + key);
		Main.keys[key]
				.playOnce((int) (doubleMult * note.getHoldCount() * gap / tempo));
	}

	public static int calculateKey(Note note, int saKey) {
		int key = -1;

		String noteName = note.getNote();
		// Set the key number of the note to be played
		if (noteName.equals("SA")) {
			key = saKey;
		} else if (noteName.equals("RE")) {
			key = saKey + 2;
		} else if (noteName.equals("GA")) {
			key = saKey + 4;
		} else if (noteName.equals("MA")) {
			key = saKey + 5;
		} else if (noteName.equals("PA")) {
			key = saKey + 7;
		} else if (noteName.equals("DHA")) {
			key = saKey + 9;
		} else if (noteName.equals("NI")) {
			key = saKey + 11;
		}

		if (note.isKomal())
			key--;
		if (note.isLower())
			key -= 12;
		if (note.isTheevra())
			key++;
		if (note.isUpper())
			key += 12;

		if (key >= 0 && key < 36)
			return key;
		else {
			String fullNote = note.getPrefix() + noteName + note.getSuffix();
			LOGGER.warning("Invalid note: " + fullNote);
			JOptionPane.showMessageDialog(null, "Error: Invalid note '"
					+ fullNote + "' is too low or too high", "Error",
					JOptionPane.ERROR_MESSAGE);
			return -1;
		}

	}

	private static boolean finish(String start) {
		if (repeat) {
			LOGGER.info("Finished. Repeating.");
			index = 0;
			if (!moveCursorToStart(start))
				return false;
			return true;
		} else {
			LOGGER.info("Finished. Returning.");
			return false;
		}
	}

	/**
	 * Sets pause to false and stop to true
	 */
	public static void stop() {
		stop = true;
		pause = false;
		isPlaying = false;
	}

	/**
	 * Sets pause to true
	 */
	public static void setPause() {
		pause = true;
		isPlaying = false;
	}

	/**
	 * If pause is true, the thread playing the shabad will sleep
	 */
	public static void pause() {
		while (pause) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns true if the playback is paused, false otherwise
	 */
	public static boolean isPaused() {
		return pause;
	}

	/**
	 * Returns true if playing, false otherwise
	 */
	public static boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * Sets isPlaying to true and pause to false.
	 */
	public static void play() {
		pause = false;
		isPlaying = true;
	}

	/**
	 * Sets the repeat flag
	 * 
	 * @param bool
	 *            - {@code repeat} is set to this value
	 */
	public static void setRepeat(boolean bool) {
		repeat = bool;
	}
}

package gsingh.learnkirtan;

import gsingh.learnkirtan.keys.Key;

import java.util.Scanner;
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
	private static final String PATTERN = "[A-Z.'\\-#]+";

	private static boolean stop = false;
	private static boolean pause = false;
	private static boolean finished = false;
	private static boolean repeat = false;
	private static Key[] keys = Main.keys;
	private static int key = 0;

	private static int holdCount;
	private static String note;
	private static String nextNote;

	private static Scanner scanner = null;

	/**
	 * Plays the shabad on the keyboard
	 * 
	 * @param shabad
	 *            - The shabad to play
	 * @param tempo
	 *            - The speed multiplier
	 */
	public static void parseAndPlay(String shabad, String start, String end,
			double tempo) {

		start = start.toUpperCase();
		end = end.toUpperCase();
		shabad = shabad.toUpperCase();
		LOGGER.info("Shabad: " + shabad);

		if (!validateShabad(shabad, start, end)) {
			JOptionPane
					.showMessageDialog(
							null,
							"Error: You specified that playback should start/stop at a label, "
									+ "but that label could not be found. Make sure there is a "
									+ "'#' before the label.", "Error",
							JOptionPane.ERROR_MESSAGE);
			return;
		}

		// TODO: Make labels work with numbers
		reset(shabad, start);

		while (!stop) {

			// Pause the thread if necessary
			if (isPaused())
				pause();

			// If the end label is found, finish. If another label is found,
			// skip it.
			if (note != null) {
				while (note.charAt(0) == '#') {
					if (!end.equals("")) {
						if (note.equals("#" + end)) {
							LOGGER.info("Reached end label");
							finished = true;
						}
					}
					note = nextNote;
					nextNote = getNextNote();

					LOGGER.info("Label skipped");

					LOGGER.info("Next note to be played: " + note);
					LOGGER.info("Next nextNote to be played: " + nextNote);

					if (note == null) {
						LOGGER.info("Reached end after label - note is null");
						finished = true;
						break;
					}
				}
			} else {
				LOGGER.info("Reached end - note is null");
				finished = true;
			}

			// If we have reached the end of the shabad or specified lines,
			// check if we should repeat. Otherwise, break.
			if (finished) {
				if (repeat) {
					LOGGER.info("Finished. Repeating.");
					reset(shabad, start);
					finished = false;
					continue;
				} else {
					LOGGER.info("Finished. Returning.");
					break;
				}
			}

			// Check if we've reached the end of the shabad or specified lines
			// TODO: Unnecessary?
			if (nextNote == null) {
				LOGGER.info("Reached end - nextNote is null.");
				finished = true;
			}

			// Determine the length of the prefix
			int count = 0;
			if (note.length() > 1) {
				LOGGER.info("Checking for prefix.");
				for (int i = 0; i < 3; i++) {
					if (note.substring(i, i + 1).matches("[A-Z\\-]"))
						break;
					count++;
				}
			}

			LOGGER.info("Prefix Length: " + count);

			// Break the input into a prefix, a suffix and a note
			String prefix = note.substring(0, count);
			String suffix = "";
			note = note.substring(count);
			int index = note.indexOf(".");
			if (index == -1)
				index = note.indexOf("'");
			if (index != -1) {
				suffix = note.substring(index);
				note = note.substring(0, index);
			}

			LOGGER.info("Prefix: " + prefix);
			LOGGER.info("Note: " + note);
			LOGGER.info("Suffix: " + suffix);

			// Set the key number of the note to be played
			if (note.equals("SA")) {
				key = 10;
			} else if (note.equals("RE")) {
				key = 12;
			} else if (note.equals("GA")) {
				key = 14;
			} else if (note.equals("MA")) {
				key = 15;
			} else if (note.equals("PA")) {
				key = 17;
			} else if (note.equals("DHA")) {
				key = 19;
			} else if (note.equals("NI")) {
				key = 21;
			} else {
				LOGGER.warning("Invalid note: " + note);
				JOptionPane.showMessageDialog(null, "Error: Invalid note '"
						+ note + "'", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			}

			// Apply the modifiers in the prefix and suffix to calculate the
			// actual key number
			// TODO: Check if notes have valid modifiers
			if (prefix.contains("'")) {
				key--;
			}
			if (prefix.contains(".")) {
				key -= 12;
			}
			if (suffix.contains("'")) {
				key++;
			}
			if (suffix.contains(".")) {
				key += 12;
			}

			if (key > 0 && key < 48) {
				LOGGER.info("Key: " + key);

				keys[key].playOnce((int) (holdCount * gap / tempo));
				note = nextNote;
				nextNote = getNextNote();

				LOGGER.info("Next note to be played: " + note);
				LOGGER.info("Next nextNote to be played: " + nextNote);

				// If note is equal to a dash, we've reached the end of the file
				if (note != null)
					if (note.equals("-")) {
						LOGGER.info("Dash reached at end of file.");
						finished = true;
					}
			} else {
				LOGGER.warning("Invalid note: " + note);
				JOptionPane.showMessageDialog(null, "Error: Invalid note '"
						+ note + "'", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
		}

		LOGGER.info("Left Loop. Returning.");
		stop = false;
		finished = false;
	}

	/**
	 * Gets the next note if one exists
	 * 
	 * @param holdCount
	 *            - this is incremented each time a dash is found
	 * @return the next note (after skipping any dashes) if it exists. If there
	 *         is no next note, return null
	 */
	private static String getNextNote() {
		String next = null;
		holdCount = 1;
		while (scanner.hasNext(PATTERN)) {
			next = scanner.next(PATTERN);
			if (next.equals("-"))
				holdCount++;
			else
				break;
		}

		return next;
	}

	/**
	 * Sets the state of the scanner so we are starting from the beginning
	 * 
	 * @param shabad
	 *            - the shabad to reset
	 * @param start
	 *            - the point in the shabad to reset too.
	 */
	private static void reset(String shabad, String start) {
		scanner = new Scanner(shabad);
		note = getFirstNote(start);
		nextNote = getNextNote();
	}

	/**
	 * Gets the first note to parse and sets the scanner to that position.
	 * 
	 * @param scanner
	 *            - the scanner reading the shabad
	 * @return the first note of the shabad
	 */
	private static String getFirstNote(String start) {
		String note;
		if (!start.equals("")) {
			LOGGER.info("Searching for starting label");
			note = scanner.next(PATTERN);
			while (!note.equals("#" + start)) {
				LOGGER.info("While searching, skipped: " + note);
				note = scanner.next(PATTERN);
			}
		}

		note = scanner.next("[A-Z.'#]+");

		return note;
	}

	/**
	 * Checks whether the shabad input is valid. Checks whether labels are
	 * present and in valid format
	 * 
	 * @return true if input is valid. False otherwise.
	 */
	private static boolean validateShabad(String shabad, String start,
			String end) {
		if (!start.equals("")) {
			LOGGER.info("A starting label was specified.");
			if (!shabad.contains("#" + start)) {
				LOGGER.warning("No starting label was found. Stopping playback.");
				return false;
			}
		}

		if (!end.equals("")) {
			LOGGER.info("An ending label was specified.");
			if (!shabad.contains("#" + end)) {
				LOGGER.warning("No ending label was found. Stopping playback.");
				return false;
			}
		}

		LOGGER.info("Validation completed successfully");
		return true;
	}

	/**
	 * Sets pause to false and stop to true
	 */
	public static void stop() {
		stop = true;
		pause = false;
	}

	/**
	 * Sets pause to true
	 */
	public static void setPause() {
		pause = true;
	}

	/**
	 * If pause is true, the thread playing the shabad will sleep
	 */
	public static void pause() {
		while (pause) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
	 * Sets pause to false, so that playback resumes. This is note used to play
	 * the shabad, only to unpause.
	 */
	public static void play() {
		pause = false;
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

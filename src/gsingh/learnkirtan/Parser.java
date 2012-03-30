package gsingh.learnkirtan;

import gsingh.learnkirtan.keys.Key;

import java.util.Scanner;

import javax.swing.JOptionPane;

public class Parser {

	/**
	 * The default length each note is played
	 */
	private static final int gap = 500;

	private static boolean stop = false;
	private static boolean pause = false;
	private static boolean finished = false;
	private static boolean repeat = false;
	private static boolean onlyAsthai = false;
	private static boolean onlyAnthra = false;
	private static Key[] keys = Main.keys;
	private static int key = 0;

	/**
	 * Plays the shabad on the keyboard
	 * 
	 * @param shabad
	 *            - The shabad to play
	 * @param tempo
	 *            - The speed multiplier
	 */
	public static void parseAndPlay(String shabad, double tempo) {

		int holdCount;

		shabad = shabad.toUpperCase();
		System.out.println(shabad);
		validateShabad(shabad);

		Scanner scanner = new Scanner(shabad);
		String note = reset(scanner);
		String next = null;

		while (!stop) {

			if (onlyAsthai)
				if (note.equals("ANTHRA"))
					finished = true;

			if (onlyAnthra)
				if (note.equals("ASTHAI"))
					finished = true;

			holdCount = 1;

			// Pause the thread if necessary
			if (isPaused())
				pause();

			// If we have reached the end of the shabad or specified lines,
			// check if we should repeat. Otherwise, break.
			if (finished) {
				if (repeat) {
					scanner = new Scanner(shabad);
					note = reset(scanner);
					next = null;
					finished = false;
				} else {
					break;
				}
			}

			// If a label is found, skip it
			if (note.equals("ASTHAI") || note.equals("ANTHRA")) {
				note = scanner.next("[A-Za-z.']+");
				continue;
			}

			// Check if we've reached the end of the shabad or specified lines
			if (!scanner.hasNext("[A-Za-z.'-]+"))
				finished = true;

			// Get the next note if there is one and check if it's a dash. If
			// so, increase the holdCount by one
			while (scanner.hasNext("[A-Za-z.'-]+")) {
				next = scanner.next("[A-Za-z.'-]+");
				if (next.equals("-"))
					holdCount++;
				else
					break;
			}

			// Determine the length of the prefix
			int count = 0;
			for (int i = 0; i < 3; i++) {
				if (note.substring(i, i + 1).matches("[A-Za-z-]"))
					break;
				count++;
			}

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

			System.out.println(prefix + note + suffix);

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
				System.out.println("Invalid note.");
				JOptionPane.showMessageDialog(null, "Error: Invalid note.",
						"Error", JOptionPane.ERROR_MESSAGE);
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
			System.out.println(pause);
			if (key > 0 && key < 48) {
				keys[key].playOnce((int) (holdCount * gap / tempo));
				note = next;

				// If note is equal to a dash, we've reached the end of the file
				if (note.equals("-"))
					finished = true;
			} else {
				System.out.println("Invalid note.");
				JOptionPane.showMessageDialog(null, "Error: Invalid note.",
						"Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
		}

		stop = false;
		finished = false;
	}

	/**
	 * Gets the first note to parse and sets the scanner to that position. The
	 * note returned depends on whether onlyAsthai or onlyAntra are set
	 * 
	 * @param scanner
	 *            - the scanner reading the shabad
	 * @return the first note of the shabad
	 */
	private static String reset(Scanner scanner) {
		String note;
		if (onlyAsthai) {
			note = scanner.next("[A-Za-z.']+");
			while (!note.equals("ASTHAI"))
				note = scanner.next("[A-Za-z.']+");
		} else if (onlyAnthra) {
			note = scanner.next("[A-Za-z.']+");
			while (!note.equals("ANTHRA"))
				note = scanner.next("[A-Za-z.']+");
		}

		note = scanner.next("[A-Za-z.']+");

		return note;
	}

	/**
	 * Checks whether the shabad input is valid. Specifically, correct use of
	 * asthai/antra labels will be checked
	 * 
	 * @return true if input is valid. False otherwise.
	 */
	private static boolean validateShabad(String shabad) {

		// First check if label is there when the checkboxes are selected
		if (onlyAsthai) {
			if (shabad.indexOf("ASTHAI") == -1) {
				System.out
						.println("Only Asthai specified but no Asthai label found");
				return false;
			}

		} else if (onlyAnthra) {
			if (shabad.indexOf("Anthra") == -1) {
				System.out
						.println("Only Anthra specified but no Anthra label found");
				return false;
			}
		}

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

	/**
	 * Sets the repeat flag
	 * 
	 * @param bool
	 *            - {@code onlyAnthra} is set to this value
	 */
	public static void setOnlyAsthai(boolean bool) {
		onlyAsthai = bool;
	}

	/**
	 * Sets the repeat flag
	 * 
	 * @param bool
	 *            - {@code onlyAnthra} is set to this value
	 */
	public static void setOnlyAnthra(boolean bool) {
		onlyAnthra = bool;
	}
}

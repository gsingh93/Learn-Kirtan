package gsingh.learnkirtan.parser;

import gsingh.learnkirtan.keys.LabelManager.Octave;
import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.note.Note.Length;
import gsingh.learnkirtan.note.Note.Modifier;
import gsingh.learnkirtan.shabad.Shabad;
import gsingh.learnkirtan.shabad.ShabadNotes;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used for parsing raw shabad text into a {@link Shabad} object
 * 
 * @author Gulshan
 * 
 */
public class Parser {

	/** The regex defining a modifier */
	private static final String modifierRegex = "(?:\\.|'|\\.'|'\\.)";

	/** The regex defining a note */
	private static final String noteRegex = String.format(
			"(?:(%s?)(sa|re|ga|ma|pa|dha|ni)(%s?))", modifierRegex,
			modifierRegex);

	/** The regex defining a shabad */
	private static final String completeNoteRegex = String.format("(?:%s(?:-%s)? ?)",
			noteRegex, noteRegex);

	/** The pattern defining a shabad */
	private static final Pattern completeNotePattern = Pattern.compile(completeNoteRegex,
			Pattern.CASE_INSENSITIVE);

	/**
	 * Parses text into a {@link Shabad} object
	 * 
	 * @param shabadText
	 *            the text to parse
	 * @return a {@link Shabad} object representing the text
	 */
	public ShabadNotes parse(String shabadText) {
		
		shabadText = shabadText.trim();
		ShabadNotes shabad = new ShabadNotes();

		List<String> tokenList = Arrays.asList(shabadText.split("\\s+"));

		for (int i = 0; i < tokenList.size(); i++) {
			String word = tokenList.get(i);

			// Ideally there shouldn't be empty strings but it happens
			if (word.equals("")) {
				continue;
			}

			Matcher matcher = completeNotePattern.matcher(word);
			
			if (word.equals("null")) {
				shabad.addNote(null);
			} else if (isLabel(word)) {
				shabad.addLabel(word, i);
			} else if (matcher.matches()) {
				// Get the captured groups of the first note
				String prefix1 = matcher.group(1);
				String name1 = matcher.group(2);
				String suffix1 = matcher.group(3);

				// Get the captured groups of the second optional note
				String prefix2 = matcher.group(4);
				String name2 = matcher.group(5);
				String suffix2 = matcher.group(6);

				// If the second note was supplied, add two half notes,
				// else add one full note
				if (name2 != null) {
					Note note1 = buildNote(prefix1, name1, suffix1, Length.HALF);
					Note note2 = buildNote(prefix2, name2, suffix2, Length.HALF);

					shabad.addNote(note1);
					shabad.addNote(note2);
				} else {
					Note note1 = buildNote(prefix1, name1, suffix1, Length.FULL);

					shabad.addNote(note1);
				}
			} else if (word.equals("-")) {
				shabad.addLongNote();
			} else {
				// TODO An unknown note was found
			}
		}

		return shabad;
	}

	/**
	 * Parses a note string to a {@link Note} object
	 * 
	 * @param noteText
	 *            the text to parse
	 * @return a {@link Note} object representing the text
	 */
	public Note parseNote(String noteText) {
		Note note = null;

		Matcher matcher = completeNotePattern.matcher(noteText);
		if (matcher.matches()) {
			String prefix = matcher.group(1);
			String name = matcher.group(2);
			String suffix = matcher.group(3);

			note = buildNote(prefix, name, suffix, Length.HALF);
		}

		return note;
	}

	/**
	 * Builds a note
	 * 
	 * @param prefix
	 *            the text before the note name
	 * @param name
	 *            the name of the note
	 * @param suffix
	 *            the text after the note name
	 * @param length
	 *            the length the note should be played in milliseconds
	 * @return a {@link Note} object built from the parameters
	 */
	private Note buildNote(String prefix, String name, String suffix,
			Length length) {
		Octave octave = Octave.MIDDLE;
		Modifier modifier = Modifier.NONE;
		if (prefix.contains("."))
			octave = Octave.LOWER;
		else if (suffix.contains("."))
			octave = Octave.UPPER;
		if (prefix.contains("'"))
			modifier = Modifier.KOMAL;
		else if (suffix.contains("'"))
			modifier = Modifier.THEEVRA;
		return new Note(name, octave, modifier, length);
	}

	/**
	 * Checks if {@code word} is a label
	 * 
	 * @param word
	 *            the word to check
	 * @return true if {@code word} is a label, false otherwise
	 */
	private boolean isLabel(String word) {
		if (word.charAt(0) == '#')
			return true;
		else
			return false;
	}

	/**
	 * Checks if {@code word} is a note
	 * 
	 * @param word
	 *            the word to check
	 * @return true if {@code word} is a note, false otherwise
	 */
	private boolean isNote(String word) {
		return true; // TODO
	}
}

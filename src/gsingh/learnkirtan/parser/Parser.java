package gsingh.learnkirtan.parser;

import gsingh.learnkirtan.keys.LabelManager.Octave;
import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.note.Note.Modifier;
import gsingh.learnkirtan.shabad.Shabad;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private static final int FULL_NOTE = 1000;
	private static final int HALF_NOTE = 500;

	private static final String modifierRegex = "(?:\\.|'|\\.'|'\\.)";
	private static final String noteRegex = String.format(
			"(?:(%s?)(sa|re|ga|ma|pa|dha|ni)(%s?))", modifierRegex,
			modifierRegex);
	private static final String regex = String.format("(?:%s(?:-%s)? ?)+",
			noteRegex, noteRegex);
	private static final Pattern pattern = Pattern.compile(regex,
			Pattern.CASE_INSENSITIVE);

	public Shabad parse(String shabadText) {
		Shabad shabad = new Shabad();

		shabadText = shabadText.toUpperCase().trim();

		// TODO: Convert to exception
		if (Validator.validate(shabadText)) {
			shabad.setShabadText(shabadText);
			List<String> wordList = Arrays.asList(shabadText.split("\\s+"));

			for (int i = 0; i < wordList.size(); i++) {
				String word = wordList.get(i);
				if (isLabel(word)) {
					shabad.addLabel(word, i);
				} else if (isNote(word)) {

					Matcher matcher = pattern.matcher(word);
					if (matcher.matches()) {
						// for (int j = 1; j <= matcher.groupCount(); j++) {
						// System.out.println(j);
						// System.out.println(matcher.group(j));
						// }
						String prefix1 = matcher.group(1);
						String name1 = matcher.group(2);
						String suffix1 = matcher.group(3);

						String prefix2 = matcher.group(4);
						String name2 = matcher.group(5);
						String suffix2 = matcher.group(6);

						if (name2 != null) {
							Note note1 = buildNote(prefix1, name1, suffix1,
									HALF_NOTE);
							Note note2 = buildNote(prefix2, name2, suffix2,
									HALF_NOTE);

							shabad.addNote(note1);
							shabad.addNote(note2);
						} else {
							Note note1 = buildNote(prefix1, name1, suffix1,
									FULL_NOTE);

							shabad.addNote(note1);
						}
					} else if (word.equals("-")) {
						shabad.addLongNote();
					}
				} else {
					System.out.println("Invalid Note " + word);
				}
			}
		}
		return shabad;
	}

	public Note parseNote(String noteText) {
		Note note = null;

		Matcher matcher = pattern.matcher(noteText);
		if (matcher.matches()) {
			// for (int j = 1; j <= matcher.groupCount(); j++) {
			// System.out.println(j);
			// System.out.println(matcher.group(j));
			// }
			String prefix1 = matcher.group(1);
			String name1 = matcher.group(2);
			String suffix1 = matcher.group(3);

			note = buildNote(prefix1, name1, suffix1, HALF_NOTE);
		}

		return note;
	}

	private Note buildNote(String prefix, String name, String suffix, int length) {
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

	private boolean isLabel(String word) {
		if (word.charAt(0) == '#')
			return true;
		else
			return false;
	}

	private boolean isNote(String word) {
		return true;
	}
}

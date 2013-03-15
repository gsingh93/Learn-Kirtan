package gsingh.learnkirtan.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for validating textual representations of shabads
 * 
 * @author Gulshan
 * 
 */
public class Validator {

	/** The regex defining a modifier */
	private static final String modifierRegex = "(?:\\.|'|\\.'|'\\.)";

	/** The regex partially defining a note */
	private static final String partialNoteRegex = String
			.format("(?:%s?(?:sa|re|ga|ma|pa|dha|ni)%s?)", modifierRegex,
					modifierRegex);

	/** The regex defining a note */
	private static final String noteRegex = String.format("(%s)(?:-(%s))?",
			partialNoteRegex, partialNoteRegex);

	private static String doublePeriodRegex = "'?\\.'?sa'?\\.'?";
	private static String doubleApostropheRegex = "\\.?'\\.?sa\\.?'\\.?";
	private static String wrongTheevraRegex = ".*(?:sa|re|ga|pa|dha|ni).*'.*";
	private static String wrongKomalRegex = ".*'.*(?:sa|ma|pa).*";
	
	private static Pattern doublePeriod = Pattern.compile(doublePeriodRegex,
			Pattern.CASE_INSENSITIVE);
	private static Pattern doubleApostrophe = Pattern.compile(
			doubleApostropheRegex, Pattern.CASE_INSENSITIVE);
	private static Pattern wrongTheevra = Pattern.compile(wrongTheevraRegex,
			Pattern.CASE_INSENSITIVE);
	private static Pattern wrongKomal = Pattern.compile(wrongKomalRegex,
			Pattern.CASE_INSENSITIVE);
	private static Pattern notePattern = Pattern.compile(noteRegex,
			Pattern.CASE_INSENSITIVE);
	
	/**
	 * Checks if the shabad is well-formed
	 * 
	 * @param shabadText
	 *            the text to validate
	 * @return a {@link ValidationErrors} object
	 */
	public static boolean validate(String note) {
		Matcher m = notePattern.matcher(note);
		if (m.matches()) {
			String note1 = m.group(1);
			String note2 = m.group(2);

			if (!validateSingleNote(note1)) {
				return false;
			}

			if (note2 != null) {
				if (!validateSingleNote(note2)) {
					return false;
				}
			}
			return true;
		} else if (note.equals("-")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean validateSingleNote(String note) {
		if (doublePeriod.matcher(note).matches()) {
			return false;
		} else if (doubleApostrophe.matcher(note).matches()) {
			return false;
		} else if (wrongTheevra.matcher(note).matches()) {
			return false;
		} else if (wrongKomal.matcher(note).matches()) {
			return false;
		}

		return true;
	}
}

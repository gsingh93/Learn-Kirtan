package gsingh.learnkirtan.validation;

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

	/** The regex defining a note */
	private static final String noteRegex = String
			.format("(?:%s?(?:sa|re|ga|ma|pa|dha|ni)%s?)", modifierRegex,
					modifierRegex);

	/** The regex defining a shabad */
	private static final String shabadRegex = String.format(
			"(?:(?:(?:%s(?:-%s)?)|-) ?)+", noteRegex, noteRegex);

	/** The pattern defining a shabad */
	private static final Pattern shabadPattern = Pattern.compile(shabadRegex,
			Pattern.CASE_INSENSITIVE);

	private static String doublePeriodRegex = "'?\\.'?sa'?\\.'?";
	private static String doubleApostropheRegex = "\\.?'\\.?sa\\.?'\\.?";
	private static String theevraMaRegex;

	/**
	 * Checks if the shabad is well-formed
	 * 
	 * @param shabadText
	 *            the text to validate
	 * @return a {@link ValidationErrors} object
	 */
	public static boolean validate(String note) {
		// ValidationErrors errors = new ValidationErrors();
		// return errors; // TODO
		if (!Pattern.matches(noteRegex, note)) {
			return false;
		} else if (Pattern.matches(doublePeriodRegex, note)) {
			return false;
		} else if (Pattern.matches(doubleApostropheRegex, note)) {
			return false;
		}
		return true;
	}

	/**
	 * Ensures that no double periods are found within the text
	 * 
	 * @return true if the condition above is true, false otherwise
	 */
	private static boolean noDoublePeriod() {
		return true; // TODO
	}

	/**
	 * Ensures that
	 * 
	 * @return true if the condition above is true, false otherwise
	 */
	private static boolean noDoubleApostrophe() {
		return true; // TODO
	}

	/**
	 * Ensures that
	 * 
	 * @return true if the condition above is true, false otherwise
	 */
	private static boolean correctTheevras() {
		return true; // TODO
	}

	/**
	 * Ensures that
	 * 
	 * @return true if the condition above is true, false otherwise
	 */
	private static boolean correctKomals() {
		return true; // TODO
	}
}

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

	private static String doublePeriodRegex = "'?\\.'?sa'?\\.'?";
	private static String doubleApostropheRegex = "\\.?'\\.?sa\\.?'\\.?";
	private static String wrongTheevraRegex = ".*(sa|re|ga|pa|dha|ni).*'.*";
	private static String wrongKomalRegex = ".*'.*(sa|ma|pa).*";
	
	private static Pattern doublePeriod = Pattern.compile(doublePeriodRegex);
	private static Pattern doubleApostrophe = Pattern
			.compile(doubleApostropheRegex);
	private static Pattern wrongTheevra = Pattern.compile(wrongTheevraRegex);
	private static Pattern wrongKomal = Pattern.compile(wrongKomalRegex);
	private static Pattern notePattern = Pattern.compile(noteRegex);
	
	/**
	 * Checks if the shabad is well-formed
	 * 
	 * @param shabadText
	 *            the text to validate
	 * @return a {@link ValidationErrors} object
	 */
	public static boolean validate(String note) {
		if (!notePattern.matcher(note).matches()) {
			return false;
		} else if (doublePeriod.matcher(note).matches()) {
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

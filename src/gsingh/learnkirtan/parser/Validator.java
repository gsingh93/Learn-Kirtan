package gsingh.learnkirtan.parser;

import java.util.regex.Pattern;

public class Validator {

	private static final String modifierRegex = "(?:\\.|'|\\.'|'\\.)";
	private static final String noteRegex = String
			.format("(?:%s?(?:sa|re|ga|ma|pa|dha|ni)%s?)", modifierRegex,
					modifierRegex);
	private static final String regex = String.format(
			"(?:(?:(?:%s(?:-%s)?)|-) ?)+", noteRegex, noteRegex);
	private static final Pattern pattern = Pattern.compile(regex,
			Pattern.CASE_INSENSITIVE);

	// TODO: Implement
	private String doublePeriodRegex = "'?\\.'?sa'?\\.'?";
	private String doubleApostropheRegex = "\\.?'\\.?sa\\.?'\\.?";
	private String theevraMaRegex;

	/**
	 * Checks if the shabad is mostly well-formed. Does not check if modifiers
	 * such as . or ' are used both before and after the note.
	 */
	public static boolean validate(String shabadText) {
		return pattern.matcher(shabadText).matches();
	}
}

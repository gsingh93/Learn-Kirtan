package gsingh.learnkirtan;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Note {
	private String name = "";
	private String prefix = "";
	private String suffix = "";
	private boolean isKomal = false;
	private boolean isTheevra = false;
	private boolean isLower = false;
	private boolean isUpper = false;
	private boolean isValid = true;
	private int holdCount = 1;

	List<String> notes = null;

	private final static Logger LOGGER = Logger.getLogger(Note.class.getName());

	static {
		LOGGER.addHandler(Main.logFile);
		LOGGER.setLevel(Level.INFO);
	}

	public Note(String note) {
		notes = Arrays.asList(new String[] { "SA", "RE", "GA", "MA", "PA",
				"DHA", "NI" });

		name = note.toUpperCase();

		int count = 0;
		if (name.length() > 1) {
			LOGGER.info("Checking for prefix.");
			for (int i = 0; i < 2; i++) {
				if (name.charAt(i) != '.' && name.charAt(i) != '\'')
					break;
				count++;
			}
		} else {
			LOGGER.warning("Invalid Note Length");
			isValid = false;
			return;
		}

		// Break the input into a prefix, a suffix and a note
		prefix = name.substring(0, count);
		suffix = "";
		name = name.substring(count);
		int index1 = name.indexOf(".");
		int index2 = name.indexOf("'");
		if (index1 != -1 && index2 != -1) {
			if (index1 < index2) {
				suffix = name.substring(index1);
				name = name.substring(0, index1);
			} else {
				suffix = name.substring(index2);
				name = name.substring(0, index2);
			}
		} else if (index1 != -1 && index2 == -1) {
			suffix = name.substring(index1);
			name = name.substring(0, index1);
		} else if (index1 == -1 && index2 != -1) {
			suffix = name.substring(index2);
			name = name.substring(0, index2);
		}

		validateAffix(prefix, true);
		validateAffix(suffix, false);

		if ((isTheevra && isKomal) || (isUpper && isLower))
			isValid = false;

		validateNote(name);
	}

	private void validateAffix(String s, boolean prefix) {
		int count1 = 0;
		int count2 = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '.')
				count1++;
			else if (s.charAt(i) == '\'')
				count2++;
			else
				isValid = false;
		}

		if (prefix) {
			if (count1 == 1)
				isLower = true;
			if (count2 == 1)
				isKomal = true;
		} else {
			if (count1 == 1)
				isUpper = true;
			if (count2 == 1)
				isTheevra = true;
		}

		if (count1 >= 2) {
			isValid = false;
			LOGGER.warning("Invalid Prefix or Suffix: " + s);
		} else if (count2 >= 2) {
			isValid = false;
			LOGGER.warning("Invalid Prefix or Suffix: " + s);
		}
	}

	public void validateNote(String note) {
		if (!notes.contains(note)) {
			LOGGER.warning("Invalid Note Name:" + note);
			isValid = false;
			return;
		}

		if (note.equals("MA") && isTheevra) {
			LOGGER.warning("Invalid Note Name: " + note);
			isValid = false;
			return;
		}

		if ((note.equals("RE") || note.equals("GA") || note.equals("DHA") || note
				.equals("NI")) && isKomal) {
			LOGGER.warning("Invalid Note Name: " + note);
			isValid = false;
			return;
		}
	}

	public String getNote() {
		return name;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public boolean isKomal() {
		return isKomal;
	}

	public boolean isTheevra() {
		return isTheevra;
	}

	public boolean isLower() {
		return isLower;
	}

	public boolean isUpper() {
		return isUpper;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setHoldCount(int count) {
		holdCount = count;
	}

	public int getHoldCount() {
		return holdCount;
	}

}

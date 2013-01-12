package gsingh.learnkirtan.note;

import gsingh.learnkirtan.keys.LabelManager.Octave;

import java.io.Serializable;

public class Note implements Serializable {

	public enum Modifier {
		NONE, KOMAL, THEEVRA;
	}

	/**
	 * The full string representation of the note, including octaves and
	 * modifiers
	 */
	private String noteText;

	/** The name of the note with no modifiers */
	private String name;

	/** The octave of the note */
	private Octave octave;

	/** The modifier (theevra or komal) of the note */
	private Modifier modifier;

	/** The length that the note should be played in milliseconds */
	private int length;

	public Note(String name, Octave octave, Modifier modifier, int length) {
		this.name = name;
		this.octave = octave;
		this.modifier = modifier;
		this.length = length;

		String noteText = name;
		if (modifier == Modifier.KOMAL)
			noteText = "'" + noteText;
		else if (modifier == Modifier.THEEVRA)
			noteText = noteText + "'";

		if (octave == Octave.LOWER)
			noteText = "." + noteText;
		else if (octave == Octave.UPPER)
			noteText = noteText + ".";

		this.noteText = noteText;
	}

	/** @return the length the note should be played in milliseconds */
	public int getLength() {
		return length;
	}

	/**
	 * Sets the length that the note should be played
	 * 
	 * @param length
	 *            the length in milliseconds
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/** @return the note name with all modifiers */
	public String getNoteText() {
		return noteText;
	}

	/** @return the note name with no modifiers in titlecase */
	public String getName() {
		// Convert to title case
		name = name.toLowerCase();
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		return name;
	}

	/** @return true if the note is komal, false otherwise */
	public boolean isKomal() {
		if (modifier == Modifier.KOMAL) {
			return true;
		} else {
			return false;
		}
	}

	/** @return true if the note is theevra, false otherwise */
	public boolean isTheevra() {
		if (modifier == Modifier.THEEVRA) {
			return true;
		} else {
			return false;
		}
	}

	/** @return true if the note is in the upper octave, false otherwise */
	public boolean isUpperOctave() {
		if (octave == Octave.UPPER) {
			return true;
		} else {
			return false;
		}
	}

	/** @return true if the note is in the lower octave, false otherwise */
	public boolean isLowerOctave() {
		if (octave == Octave.LOWER) {
			return true;
		} else {
			return false;
		}
	}
}

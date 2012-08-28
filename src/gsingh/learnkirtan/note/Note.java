package gsingh.learnkirtan.note;

import gsingh.learnkirtan.keys.Key;
import gsingh.learnkirtan.keys.KeyMapper;
import gsingh.learnkirtan.keys.LabelManager.Octave;

public class Note {

	public enum Modifier {
		NONE, KOMAL, THEEVRA;
	}

	private String noteText;
	private String name;
	private Octave octave;
	private Modifier modifier;
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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void play() {
		Key key = KeyMapper.getInstance().getKeyFromNote(this);
		key.playOnce(length);
	}

	public String getNoteText() {
		return noteText;
	}

	public String getName() {
		// Convert to title case
		name = name.toLowerCase();
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		return name;
	}

	public boolean isKomal() {
		if (modifier == Modifier.KOMAL) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isTheevra() {
		if (modifier == Modifier.THEEVRA) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isUpperOctave() {
		if (octave == Octave.UPPER) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLowerOctave() {
		if (octave == Octave.LOWER) {
			return true;
		} else {
			return false;
		}
	}
}

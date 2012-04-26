package gsingh.learnkirtan.note;


public class DoubleNote {
	private Note note1 = null;
	private Note note2 = null;

	public DoubleNote(String input) {
		if (input.matches("[.']*[A-Z]{2,3}[.']*\\-[.']*[A-Z]{2,3}[.']*")) {
			int index = input.indexOf("-");
			note1 = new Note(input.substring(0, index));
			note2 = new Note(input.substring(index + 1));
		} else
			note1 = new Note(input);
	}

	public Note getNote1() {
		return note1;
	}

	public Note getNote2() {
		return note2;
	}
}

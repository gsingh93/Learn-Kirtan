package gsingh.learnkirtan.keys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import gsingh.learnkirtan.Constants;
import gsingh.learnkirtan.keys.LabelManager.Octave;
import gsingh.learnkirtan.note.Note;
import gsingh.learnkirtan.note.Note.Length;
import gsingh.learnkirtan.note.Note.Modifier;
import gsingh.learnkirtan.note.NoteList;
import gsingh.learnkirtan.parser.exceptions.NoteOutOfBoundsException;

import org.junit.BeforeClass;
import org.junit.Test;

public class KeyMapperTest {

	private static KeyMapper km = KeyMapper.getInstance();

	@BeforeClass
	public static void setup() {
		NoteList notes = new NoteList(10);
		LabelManager labelManager = new LabelManager(notes);

		Key[] keys = new Key[Constants.MAX_KEYS];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = new Key(labelManager);
		}
		km.setKeys(keys);
	}

	@Test
	public void getKeyFromName_ShudLowerOctaveNote_ReturnCorrectKey() {
		Key key = null;
		try {
			key = km.getKeyFromNote(new Note("ma", Octave.LOWER,
					Modifier.THEEVRA, Length.FULL));
		} catch (NoteOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(44, key.getMidiNoteId());
	}

	@Test
	public void getKeyFromName_ShudMiddleOctaveNote_ReturnCorrectKey() {
		Key key = null;
		try {
			key = km.getKeyFromNote(new Note("sa", Octave.MIDDLE,
					Modifier.NONE, Length.FULL));
		} catch (NoteOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(50, key.getMidiNoteId());
	}

	@Test
	public void getKeyFromName_ShudUpperOctaveNote_ReturnCorrectKey() {
		Key key = null;
		try {
			key = km.getKeyFromNote(new Note("re", Octave.UPPER,
					Modifier.KOMAL, Length.FULL));
		} catch (NoteOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(63, key.getMidiNoteId());
	}

	@Test
	public void getKeyFromName_OutOfBoundsNote_ReturnNull() {
		Key key = null;
		try {
			key = km.getKeyFromNote(new Note("sa", Octave.LOWER,
					Modifier.THEEVRA, Length.FULL));
		} catch (NoteOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNull(key);
	}

	@Test
	public void shiftKeysTest() {
		fail("Not yet implemented");
	}
}

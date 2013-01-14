package gsingh.learnkirtan.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gsingh.learnkirtan.shabad.Shabad;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

	Parser parser;

	@Before
	public void setup() {
		parser = new Parser();
	}

	@Test
	public void parseSingleNoteTest() {
		validate("sa", 1);
	}

	@Test
	public void parseSingleNoteModifierTest() {
		validate("sa.", 1);
		validate("sa'", 1);
		validate("sa.'", 1);
		validate("sa'.", 1);
		validate(".sa", 1);
		validate("'sa", 1);
		validate(".'sa", 1);
		validate("'.sa", 1);
		validate(".sa'", 1);
		validate("'sa.", 1);
	}

	@Test
	public void parseDoubleNoteTest() {
		validate("sa-sa", 2);
	}

	@Test
	public void parseLongNoteTest() {
		validate("sa - re -", 2);
	}

	@Test
	public void validateMultipleSpaces() {
		validate("sa  re", 2);
	}

	@Test
	public void parseDoubleNoteModifierTest() {
		validate("sa.-.sa", 2);
		validate("sa'-'sa", 2);
		validate("sa.'-'.sa", 2);
		validate("sa'.-.'sa", 2);
		validate(".sa-sa.", 2);
		validate("'sa-sa.", 2);
		validate(".'sa-sa", 2);
		validate("'.sa-.sa'", 2);
		validate(".sa'-'.sa", 2);
		validate("'sa.-'sa.", 2);
	}

	@Test
	public void parseMultipleNoteTest() {
		validate("sa re. .ga ma' pa.' dha.' .ni 'sa .'re-'.ga", 10);
	}

	// TODO: Make custom exception
	@Test(expected = Exception.class)
	public void parseInvalidShabadTest() {
		fail("Not yet implemented");
	}

	// TODO: This only checks that the parser interpreted the right number of
	// notes, not if the notes are actually correct
	private void validate(String text, int size) {
		Shabad shabad = new Shabad(text);
		assertEquals(shabad.getShabadText(), text.toUpperCase());
		assertEquals(shabad.getNotes().size(), size);
	}
}

package gsingh.learnkirtan.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidatorTest {

	@Test
	public void validateNoteTest() {
		validate("sa");
	}

	@Test
	public void validateLowerNote() {
		validate(".sa");
	}

	@Test
	public void validateUpperNote() {
		validate("sa.");
	}

	@Test
	public void validateTheevraNote() {
		validate("sa'");
	}

	@Test
	public void validateTheevraLowerNote() {
		validate(".sa'");
	}

	@Test
	public void validateTheevraUpperNote() {
		validate("sa.'");
		validate("sa'.");
	}

	@Test
	public void validateKomalLowerNote() {
		validate(".'sa");
		validate("'.sa");
	}

	@Test
	public void validateKomalUpperNote() {
		validate("'sa.");
	}

	@Test
	public void validateLongNote() {
		validate("sa - re -");
	}

	@Test
	public void validateMultipleSpaces() {
		validate("sa  re");
	}

	@Test
	public void validateDoubleNote() {
		validate("sa-re");
	}

	@Test
	public void validateDoubleNotePeriodBefore() {
		validate(".sa-.re");
	}

	@Test
	public void validateDoubleNotePeriodAfter() {
		validate("sa-re");
	}

	@Test
	public void validateDoubleNoteApostrophe() {
		validate("sa'-re'");
	}

	@Test
	public void validateMultipleNotes() {
		validate("sa .sa sa. sa' .sa' sa.' sa'. .'sa '.sa 'sa.");
	}

	@Test
	public void invalidateDoublePeriod() {
		invalidate("..sa");
		invalidate("sa..");
	}

	@Test
	public void invalidateDoubleApostrophe() {
		invalidate("''sa");
		invalidate("sa''");
	}

	@Test
	public void invalidateMultiplePeriod() {
		invalidate(".sa.");
	}

	@Test
	public void invalidateMultipleApostrophe() {
		invalidate("'sa'");
	}

	@Test
	public void validateOnlyMaAsTheevra() {
		validate("ma'");
		invalidate("sa'");
		invalidate("re'");
		invalidate("ga'");
		invalidate("pa'");
		invalidate("dha'");
		invalidate("ni'");
	}

	@Test
	public void validateOnlyReGaDhaNiAsKomal() {
		validate("'re");
		validate("'ga");
		validate("'dha");
		validate("'ni");

		invalidate("'sa");
		invalidate("'ma");
		invalidate("'pa");
	}

	private void validate(String text) {
		boolean result = Validator.validate(text);

		assertTrue(result);
	}

	private void invalidate(String text) {
		boolean result = Validator.validate(text);

		assertFalse(result);
	}
}

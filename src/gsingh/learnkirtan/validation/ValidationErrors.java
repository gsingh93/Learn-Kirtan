package gsingh.learnkirtan.validation;

import java.util.LinkedList;
import java.util.List;

/**
 * A wrapper class for a list of {@link ValidationError}s
 * 
 * @author Gulshan
 * 
 */
public class ValidationErrors {

	/** A list of {@link ValidationError}s */
	private List<ValidationError> errors = new LinkedList<ValidationError>();

	/** @return an array of {@link ValidationError}s */
	public ValidationError[] getErrors() {
		return (ValidationError[]) errors.toArray();
	}

	/**
	 * Adds an {@link ValidationError} to the list
	 * 
	 * @param line
	 *            the line the error occurred on
	 * @param pos
	 *            the position the error occured on the line
	 * @param token
	 *            the token that cause the error
	 * @param message
	 *            the error message
	 */
	public void addError(int line, int pos, String token, String message) {
		errors.add(new ValidationError(line, pos, token, message));
	}

	/** @return true if there are no errors, false otherwise */
	public boolean noErrors() {
		if (errors.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}

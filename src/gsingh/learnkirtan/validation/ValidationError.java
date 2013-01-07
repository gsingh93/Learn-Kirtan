package gsingh.learnkirtan.validation;

public class ValidationError {

	/** The line that the error occured on */
	private int line;

	/** The position on a line that an error occurred */
	private int pos;

	/** The token that caused the error */
	private String token;

	/** The error message */
	private String message;

	public ValidationError(int line, int pos, String token, String message) {
		this.line = line;
		this.pos = pos;
		this.token = token;
		this.message = message;
	}
	
	/** @return the line of the error */
	public int getLine() {
		return line;
	}
	
	/** @return the position of the error on the line */
	public int getPos() {
		return pos;
	}
	
	/** @return the token that caused the error */
	public String getToken() {
		return token;
	}

	/** @return the error message */
	public String getMessage() {
		return message;
	}
}

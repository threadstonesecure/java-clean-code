package clean.code.challenge.exceptions;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}

	public ValidationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ValidationException(final String message) {
		super(message);
	}

	public ValidationException(final Throwable cause) {
		super(cause);
	}

}

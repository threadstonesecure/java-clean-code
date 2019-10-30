package clean.code.challenge.exceptions;

public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AppException() {
		super();
	}

	public AppException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public AppException(final String message) {
		super(message);
	}

	public AppException(final Throwable cause) {
		super(cause);
	}

}

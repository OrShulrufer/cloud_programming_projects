package RatingService;

public class ExistException extends RuntimeException {
	
	private static final long serialVersionUID = 3650632034487582468L;

	
	public ExistException() {
	}

	public ExistException(String message) {
		super(message);
	}

	public ExistException(Throwable cause) {
		super(cause);
	}

	public ExistException(String message, Throwable cause) {
		super(message, cause);
	}
}

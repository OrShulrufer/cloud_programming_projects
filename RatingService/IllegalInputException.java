package RatingService;

public class IllegalInputException extends RuntimeException {

	private static final long serialVersionUID = 3416459360987410351L;
	
	public IllegalInputException() {
	}

	public IllegalInputException(String message) {
		super(message);
	}

	public IllegalInputException(Throwable cause) {
		super(cause);
	}

	public IllegalInputException(String message, Throwable cause) {
		super(message, cause);
	}

}

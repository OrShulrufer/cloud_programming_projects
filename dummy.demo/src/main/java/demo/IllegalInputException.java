package demo;

public class IllegalInputException extends RuntimeException {
	private static final long serialVersionUID = -900694825969908712L;

	public IllegalInputException() {
	}

	public IllegalInputException(String message) {
		super(message);
	}

}

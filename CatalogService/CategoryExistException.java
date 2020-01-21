package CatalogService;

public class CategoryExistException extends RuntimeException {
	
	private static final long serialVersionUID = 4115912177382448022L;

	public CategoryExistException() {
	}

	public CategoryExistException(String message) {
		super(message);
	}

	public CategoryExistException(Throwable cause) {
		super(cause);
	}

	public CategoryExistException(String message, Throwable cause) {
		super(message, cause);
	}
}

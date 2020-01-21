package CatalogService;

public class ProductNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1854931816162489819L;

	public ProductNotFoundException() {
	}

	public ProductNotFoundException(String message) {
		super(message);
	}

	public ProductNotFoundException(Throwable cause) {
		super(cause);
	}

	public ProductNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}

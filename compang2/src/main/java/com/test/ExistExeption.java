package com.test;

public class ExistExeption extends RuntimeException{
	private static final long serialVersionUID = 0;
	
	public ExistExeption() {
		super();
	}

	public ExistExeption(String message, Throwable cause) {
		super(message, cause);
	}

	public ExistExeption(String message) {
		super(message);
	}

	public ExistExeption(Throwable cause) {
		super(cause);
	} 

}

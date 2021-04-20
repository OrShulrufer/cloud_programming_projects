package com.test;

public class IncorrectInputExeption extends RuntimeException{
	private static final long serialVersionUID = 0;
	
	public IncorrectInputExeption() {
		super();
	}

	public IncorrectInputExeption(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectInputExeption(String message) {
		super(message);
	}

	public IncorrectInputExeption(Throwable cause) {
		super(cause);
	}

}

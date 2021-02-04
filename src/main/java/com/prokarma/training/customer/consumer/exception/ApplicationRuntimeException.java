package com.prokarma.training.customer.consumer.exception;

public class ApplicationRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6342234431024947504L;

	public ApplicationRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationRuntimeException(String message) {
		super(message);
	}
}

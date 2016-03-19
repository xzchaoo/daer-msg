package org.xzc.msg.exception;

public class InvalidMessageException extends KnownException {
	public InvalidMessageException() {
	}

	public InvalidMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	}

	public InvalidMessageException(String message, Throwable cause) {
		super( message, cause );
	}

	public InvalidMessageException(String message) {
		super( message );
	}

	public InvalidMessageException(Throwable cause) {
		super( cause );
	}
	
}

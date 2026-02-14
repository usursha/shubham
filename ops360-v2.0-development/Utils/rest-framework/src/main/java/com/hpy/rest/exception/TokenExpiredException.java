package com.hpy.rest.exception;

public class TokenExpiredException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenExpiredException() {
        super("Token has expired");
    }

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}

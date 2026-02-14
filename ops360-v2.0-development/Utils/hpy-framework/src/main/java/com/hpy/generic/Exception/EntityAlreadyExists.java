package com.hpy.generic.Exception;

public class EntityAlreadyExists extends RuntimeException {

	private static final long serialVersionUID = 207713554794460615L;
	public EntityAlreadyExists(String message) {
		super(message);
	}


}

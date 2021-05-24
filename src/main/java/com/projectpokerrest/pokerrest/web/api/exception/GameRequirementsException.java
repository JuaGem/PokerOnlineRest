package com.projectpokerrest.pokerrest.web.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameRequirementsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public GameRequirementsException(String message) {
		super(message);
	}

}

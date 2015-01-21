package com.gagror;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CodingErrorException extends GagrorRuntimeException {

	public CodingErrorException(final String message) {
		super(message);
	}
}

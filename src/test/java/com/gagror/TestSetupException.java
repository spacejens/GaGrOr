package com.gagror;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Needed only to make design rules test pass
public class TestSetupException extends GagrorRuntimeException {

	public TestSetupException(final String message) {
		super(message);
	}
}

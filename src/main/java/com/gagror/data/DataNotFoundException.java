package com.gagror.data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gagror.GagrorRuntimeException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends GagrorRuntimeException {

	public DataNotFoundException(final String description) {
		super("Data not found: " + description);
	}
}

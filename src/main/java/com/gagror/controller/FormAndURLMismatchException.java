package com.gagror.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormAndURLMismatchException extends RuntimeException {

	public FormAndURLMismatchException(final String field, final Object urlValue, final Object formValue) {
		super(String.format("%s mismatch between URL (%s) and form (%s) when posting form", field, urlValue, formValue));
	}
}

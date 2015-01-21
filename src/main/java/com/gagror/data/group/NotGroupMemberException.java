package com.gagror.data.group;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gagror.GagrorRuntimeException;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotGroupMemberException extends GagrorRuntimeException {

	public NotGroupMemberException(final String message) {
		super(message);
	}
}

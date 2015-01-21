package com.gagror.data.group;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gagror.GagrorRuntimeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GroupMembershipChangeException extends GagrorRuntimeException {

	public GroupMembershipChangeException(final String message) {
		super(message);
	}
}

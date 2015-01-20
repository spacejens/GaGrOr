package com.gagror.data.group;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WrongGroupTypeException extends RuntimeException {

	public WrongGroupTypeException(final GroupEntity group) {
		super(String.format("Group '%s' (ID %d) was not of the expected type", group.getName(), group.getId()));
	}
}

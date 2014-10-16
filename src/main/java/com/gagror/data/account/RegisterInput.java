package com.gagror.data.account;

import javax.validation.constraints.Size;

import lombok.Data;
import lombok.ToString;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Data
@ToString(exclude={"password","passwordRepeat"})
public class RegisterInput {

	@Size(min=3, max=64)
	private String username;

	private String password;

	private String passwordRepeat;

	public void addErrorUsernameBusy(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "username", "must be unique"));
	}

	public void addErrorPasswordMismatch(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "passwordRepeat", "must match"));
	}
}

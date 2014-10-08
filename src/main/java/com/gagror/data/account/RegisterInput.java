package com.gagror.data.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true, exclude="passwordRepeat")
public class RegisterInput extends LoginCredentialsInput {

	private String passwordRepeat;

	public void addErrorUsernameBusy(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "username", "must be unique"));
	}

	public void addErrorPasswordMismatch(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "passwordRepeat", "must match"));
	}
}

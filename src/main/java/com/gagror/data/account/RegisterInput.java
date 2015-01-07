package com.gagror.data.account;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.gagror.data.AbstractInput;

public class RegisterInput extends AbstractInput {

	@Getter
	@Setter
	@Size(min=3, max=64)
	private String username;

	@Getter
	@Setter
	private String password;

	@Getter
	@Setter
	private String passwordRepeat;

	public void addErrorUsernameBusy(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "username", "must be unique"));
	}

	public void addErrorPasswordTooWeak(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "password", "too weak"));
	}

	public void addErrorPasswordMismatch(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "passwordRepeat", "must match"));
	}

	@Override
	public String toString() {
		return String.format("username=%s", username);
	}
}

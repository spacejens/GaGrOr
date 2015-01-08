package com.gagror.data.account;

import lombok.Getter;
import lombok.Setter;

import org.springframework.validation.BindingResult;

import com.gagror.data.AbstractNonIdentifiableNamedInput;

public class RegisterInput extends AbstractNonIdentifiableNamedInput {

	@Getter
	@Setter
	private String password;

	@Getter
	@Setter
	private String passwordRepeat;

	public void addErrorUsernameBusy(final BindingResult bindingResult) {
		AccountEditInput.createErrorUsernameBusy(bindingResult);
	}

	public void addErrorPasswordTooWeak(final BindingResult bindingResult) {
		AccountEditInput.createErrorPasswordTooWeak(bindingResult);
	}

	public void addErrorPasswordMismatch(final BindingResult bindingResult) {
		AccountEditInput.createErrorPasswordMismatch(bindingResult);
	}
}

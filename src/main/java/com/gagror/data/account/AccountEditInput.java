package com.gagror.data.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.gagror.data.AbstractIdentifiableNamedInput;

@ToString(callSuper=true)
@NoArgsConstructor
public class AccountEditInput extends AbstractIdentifiableNamedInput<Long> {

	@Getter
	@Setter
	private Long version;

	@Getter
	@Setter
	private String password;

	@Getter
	@Setter
	private String passwordRepeat;

	@Getter
	@Setter
	private boolean active;

	@Getter
	@Setter
	private boolean locked;

	@Getter
	@Setter
	private AccountType accountType;

	public AccountEditInput(final AccountEditOutput currentState) {
		super(currentState);
		setVersion(currentState.getVersion());
		setName(currentState.getName());
		setActive(currentState.isActive());
		setLocked(currentState.isLocked());
		setAccountType(currentState.getAccountType());
	}

	public void addErrorUsernameBusy(final BindingResult bindingResult) {
		createErrorUsernameBusy(bindingResult);
	}
	static void createErrorUsernameBusy(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "name", "must be unique"));
	}

	public void addErrorPasswordTooWeak(final BindingResult bindingResult) {
		createErrorPasswordTooWeak(bindingResult);
	}
	static void createErrorPasswordTooWeak(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "password", "too weak"));
	}

	public void addErrorPasswordMismatch(final BindingResult bindingResult) {
		createErrorPasswordMismatch(bindingResult);
	}
	static void createErrorPasswordMismatch(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "passwordRepeat", "must match"));
	}

	public void addErrorSimultaneuosEdit(final BindingResult bindingResult) {
		bindingResult.addError(new ObjectError(bindingResult.getObjectName(), "Simultaneous edit of this user detected, cannot proceed"));
	}

	public void addErrorDisallowedAccountType(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "accountType", "disallowed account type"));
	}
}

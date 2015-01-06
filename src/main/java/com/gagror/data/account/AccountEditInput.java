package com.gagror.data.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@NoArgsConstructor
public class AccountEditInput extends RegisterInput {

	private Long id;

	private Long version;

	private boolean active;

	private boolean locked;

	private AccountType accountType;

	public AccountEditInput(final AccountEditOutput currentState) {
		setId(currentState.getId());
		setVersion(currentState.getVersion());
		setUsername(currentState.getName());
		setActive(currentState.isActive());
		setLocked(currentState.isLocked());
		setAccountType(currentState.getAccountType());
	}

	public void addErrorSimultaneuosEdit(final BindingResult bindingResult) {
		bindingResult.addError(new ObjectError(bindingResult.getObjectName(), "Simultaneous edit of this user detected, cannot proceed"));
	}

	public void addErrorDisallowedAccountType(final BindingResult bindingResult) {
		bindingResult.addError(new FieldError(bindingResult.getObjectName(), "accountType", "disallowed account type"));
	}
}

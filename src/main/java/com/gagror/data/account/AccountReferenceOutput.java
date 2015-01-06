package com.gagror.data.account;

import lombok.Getter;

import com.gagror.data.AbstractEditableNamedEntityOutput;

public class AccountReferenceOutput
extends AbstractEditableNamedEntityOutput {

	@Getter
	private final AccountType accountType;

	@Getter
	private final String cssClass;

	public AccountReferenceOutput(final AccountEntity entity) {
		super(entity);
		accountType = entity.getAccountType();
		if(! entity.isActive()) {
			cssClass = "account.icon.inactive";
		} else if(entity.isLocked()) {
			cssClass = "account.icon.locked";
		} else {
			cssClass = getAccountType().getCssClass();
		}
	}
}

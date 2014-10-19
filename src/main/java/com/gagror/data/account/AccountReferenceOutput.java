package com.gagror.data.account;

import java.util.Date;

import lombok.Data;

@Data
public class AccountReferenceOutput {

	private final Long id;

	private final String username;

	private final AccountType accountType;

	private final Date created;

	private final String cssClass;

	public AccountReferenceOutput(final AccountEntity entity) {
		id = entity.getId();
		username = entity.getUsername();
		accountType = entity.getAccountType();
		created = entity.getCreated();
		// TODO Move the account CSS classes to properties (i.e. view layer), only return property here
		if(! entity.isActive()) {
			cssClass = "fi-skull";
		} else if(entity.isLocked()) {
			cssClass = "fi-prohibited";
		} else {
			cssClass = getAccountType().getCssClass();
		}
	}
}

package com.gagror.data.account;

import java.util.Date;

import lombok.Data;

@Data
public class AccountReferenceOutput implements Comparable<AccountReferenceOutput>{

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
		if(! entity.isActive()) {
			cssClass = "account.icon.inactive";
		} else if(entity.isLocked()) {
			cssClass = "account.icon.locked";
		} else {
			cssClass = getAccountType().getCssClass();
		}
	}

	@Override
	public int compareTo(final AccountReferenceOutput other) {
		return getUsername().compareTo(other.getUsername());
	}
}

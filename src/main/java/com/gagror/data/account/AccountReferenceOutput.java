package com.gagror.data.account;

import java.util.Date;

import lombok.Data;

@Data
public class AccountReferenceOutput {

	private final Long id;

	private final String username;

	private final AccountType accountType;

	private final Date created;

	public AccountReferenceOutput(final AccountEntity entity) {
		id = entity.getId();
		username = entity.getUsername();
		accountType = entity.getAccountType();
		created = entity.getCreated();
	}
}

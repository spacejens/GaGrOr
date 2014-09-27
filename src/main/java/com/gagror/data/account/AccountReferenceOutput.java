package com.gagror.data.account;

import lombok.Data;

@Data
public class AccountReferenceOutput {

	private final Long id;

	private final String username;

	private final AccountType accountType;

	public AccountReferenceOutput(final AccountEntity entity) {
		id = entity.getId();
		username = entity.getUsername();
		accountType = entity.getAccountType();
	}
}

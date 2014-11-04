package com.gagror.data.account;

import lombok.Getter;

public class AccountEditOutput extends AccountReferenceOutput {

	@Getter
	private final Long version;

	@Getter
	private final boolean active;

	@Getter
	private final boolean locked;

	public AccountEditOutput(final AccountEntity entity) {
		super(entity);
		version = entity.getVersion();
		active = entity.isActive();
		locked = entity.isLocked();
	}
}

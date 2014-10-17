package com.gagror.data.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class AccountEditOutput extends AccountReferenceOutput {

	private final Long version;

	public AccountEditOutput(final AccountEntity entity) {
		super(entity);
		version = entity.getVersion();
	}
}

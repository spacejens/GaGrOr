package com.gagror.data.account;

import lombok.Getter;
import lombok.ToString;

import com.gagror.data.AbstractEntityOutput;

@ToString(of="username", callSuper=true)
public class AccountReferenceOutput
extends AbstractEntityOutput
implements Comparable<AccountReferenceOutput>{

	@Getter
	private final String username;

	@Getter
	private final AccountType accountType;

	@Getter
	private final String cssClass;

	public AccountReferenceOutput(final AccountEntity entity) {
		super(entity);
		username = entity.getUsername();
		accountType = entity.getAccountType();
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

package com.gagror.data.account;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.Output;

public class CurrentUserOutput implements Output {

	@Getter
	private final AccountReferenceOutput account;

	public CurrentUserOutput(final AccountEntity requestAccount) {
		account = new AccountReferenceOutput(requestAccount);
	}

	public CurrentUserOutput() {
		account = null;
	}

	public boolean is(final AccountReferenceOutput otherAccount) {
		if(null == getAccount()) {
			return false;
		}
		return otherAccount.getId().equals(getAccount().getId());
	}

	public boolean isNot(final AccountReferenceOutput otherAccount) {
		return !is(otherAccount);
	}

	public List<AccountType> getMayEdit() {
		if(isLoggedIn()) {
			return getAccount().getAccountType().getMayEdit();
		}
		return Collections.emptyList();
	}

	public boolean mayEdit(final AccountReferenceOutput otherAccount) {
		if(is(otherAccount)) {
			return true;
		}
		return getMayEdit().contains(otherAccount.getAccountType());
	}

	// TODO Add capability to check group membership statuses (invited / full member / owner), simplify HTML

	public boolean isLoggedIn() {
		return null != account;
	}
}

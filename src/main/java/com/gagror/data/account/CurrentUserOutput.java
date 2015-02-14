package com.gagror.data.account;

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

	// TODO Add capability to check if current user is same as another user, simplify HTML
	// TODO Add capability to check group membership statuses (invited / full member / owner), simplify HTML
	// TODO Add capability to check if current user can edit other accounts, simplify HTML

	public boolean isLoggedIn() {
		return null != account;
	}
}

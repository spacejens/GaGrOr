package com.gagror.data.account;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class AccountEntityUnitTest {

	private static final String LOGIN = "username";
	private static final String PASSWORD = "secretpassword";
	private static final AccountType TYPE = AccountType.STANDARD;

	private AccountEntity instance;

	@Test
	public void toString_passwordNotIncluded() {
		final String toString = instance.toString();
		assertFalse("Account password should never be printed", toString.contains(PASSWORD));
	}

	@Before
	public void setupInstance() {
		instance = new AccountEntity();
		instance.setLogin(LOGIN);
		instance.setPassword(PASSWORD);
		instance.setAccountType(TYPE);
	}
}

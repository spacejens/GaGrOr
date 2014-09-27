package com.gagror.data.account;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import com.gagror.data.account.LoginCredentialsInput;

public class LoginCredentialsInputUnitTest {

	private static final String LOGIN = "username";
	private static final String PASSWORD = "secretpassword";

	private LoginCredentialsInput instance;

	@Test
	public void toString_passwordNotIncluded() {
		final String toString = instance.toString();
		assertFalse("Input password should never be printed", toString.contains(PASSWORD));
	}

	@Before
	public void setupInstance() {
		instance = new LoginCredentialsInput();
		instance.setLogin(LOGIN);
		instance.setPassword(PASSWORD);
	}
}

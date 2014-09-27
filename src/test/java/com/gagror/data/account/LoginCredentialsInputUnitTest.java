package com.gagror.data.account;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class LoginCredentialsInputUnitTest {

	private static final String USERNAME = "username";
	private static final String PASSWORD = "secretpassword";
	private static final String ENCRYPTED = "EnCrYpTeDpaSsWoRd";

	private LoginCredentialsInput instance;

	@Test
	public void toString_passwordNotIncluded() {
		final String toString = instance.toString();
		assertFalse("Input password should never be printed", toString.contains(PASSWORD));
	}

	@Test
	public void toString_encryptedPasswordNotIncluded() {
		final String toString = instance.toString();
		assertFalse("Encrypted password should never be printed", toString.contains(ENCRYPTED));
	}

	@Before
	public void setupInstance() {
		instance = new LoginCredentialsInput();
		instance.setUsername(USERNAME);
		instance.setPassword(PASSWORD);
		instance.setEncryptedPassword(ENCRYPTED);
	}
}

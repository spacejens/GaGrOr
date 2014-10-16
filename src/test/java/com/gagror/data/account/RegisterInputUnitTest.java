package com.gagror.data.account;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class RegisterInputUnitTest {

	private static final String USERNAME = "username";
	private static final String PASSWORD = "secretpassword";
	private static final String REPEAT = "secretrepeat";

	private RegisterInput instance;

	@Test
	public void toString_passwordNotIncluded() {
		final String toString = instance.toString();
		assertFalse("Input password should never be printed", toString.contains(PASSWORD));
	}

	@Test
	public void toString_repeatPasswordNotIncluded() {
		final String toString = instance.toString();
		assertFalse("Input password should never be printed", toString.contains(REPEAT));
	}

	@Before
	public void setupInstance() {
		instance = new RegisterInput();
		instance.setUsername(USERNAME);
		instance.setPassword(PASSWORD);
		instance.setPasswordRepeat(REPEAT);
	}
}

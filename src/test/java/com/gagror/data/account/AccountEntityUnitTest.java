package com.gagror.data.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AccountEntityUnitTest {

	private static final String USERNAME = "username";
	private static final String PASSWORD = "secretpassword";

	private AccountEntity instance;

	@Test
	public void toString_passwordNotIncluded() {
		final String toString = instance.toString();
		assertFalse("Account password should never be printed", toString.contains(PASSWORD));
	}

	@Test
	public void username() {
		assertEquals("Wrong username", USERNAME, instance.getUsername());
	}

	@Test
	public void password() {
		assertEquals("Wrong password", PASSWORD, instance.getPassword());
	}

	@Test
	public void accountType() {
		assertEquals("Wrong default account type of created account", AccountType.STANDARD, instance.getAccountType());
	}

	@Test
	public void active() {
		assertTrue("Created account should be active", instance.isActive());
	}

	@Test
	public void locked() {
		assertFalse("Created account should not be locked", instance.isLocked());
	}

	@Before
	public void setupInstance() {
		instance = new AccountEntity(USERNAME, PASSWORD);
	}
}

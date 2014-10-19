package com.gagror.data.account;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AccountTypeUnitTest {

	@Test
	public void standard_mayEdit() {
		assertMayEdit(AccountType.STANDARD);
	}

	@Test
	public void admin_mayEdit() {
		assertMayEdit(AccountType.ADMIN, AccountType.STANDARD, AccountType.ADMIN);
	}

	@Test
	public void systemOwner_mayEdit() {
		assertMayEdit(AccountType.SYSTEM_OWNER, AccountType.STANDARD, AccountType.ADMIN);
	}

	// TODO Add unit tests for authorities of account types

	private void assertMayEdit(final AccountType actor, final AccountType... expectedMayEdit) {
		final List<AccountType> expected = Arrays.asList(expectedMayEdit);
		for(final AccountType mayEdit : expectedMayEdit) {
			assertTrue(String.format("Account type %s should be able to edit %s", actor, mayEdit), actor.getMayEdit().contains(mayEdit));
		}
		for(final AccountType mayEdit : actor.getMayEdit()) {
			assertTrue(String.format("Account type %s should not be able to edit %s", actor, mayEdit), expected.contains(mayEdit));
		}
	}
}

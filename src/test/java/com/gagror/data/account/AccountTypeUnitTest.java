package com.gagror.data.account;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

public class AccountTypeUnitTest {

	// TODO Parameterize or do something else to ensure that all tests exist for all account types

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

	@Test
	public void standard_authorities() {
		assertAuthorities(AccountType.STANDARD);
	}

	@Test
	public void admin_authorities() {
		assertAuthorities(AccountType.ADMIN, SecurityRoles.ROLE_ADMIN);
	}

	@Test
	public void systemOwner_authorities() {
		assertAuthorities(AccountType.SYSTEM_OWNER, SecurityRoles.ROLE_ADMIN);
	}

	private void assertMayEdit(final AccountType actor, final AccountType... expectedMayEdit) {
		final List<AccountType> expected = Arrays.asList(expectedMayEdit);
		for(final AccountType mayEdit : expectedMayEdit) {
			assertTrue(String.format("Account type %s should be able to edit %s", actor, mayEdit), actor.getMayEdit().contains(mayEdit));
		}
		for(final AccountType mayEdit : actor.getMayEdit()) {
			assertTrue(String.format("Account type %s should not be able to edit %s", actor, mayEdit), expected.contains(mayEdit));
		}
	}

	private void assertAuthorities(final AccountType actor, final String... expectedRoles) {
		final List<String> expected = Arrays.asList(expectedRoles);
		for(final String exp : expected) {
			boolean found = false;
			for(final GrantedAuthority granted : actor.getAuthorities()) {
				if(exp.equals(granted.getAuthority())) {
					found = true;
				}
			}
			assertTrue(String.format("Account type %s should be granted %s", actor, exp), found);
		}
		for(final GrantedAuthority granted : actor.getAuthorities()) {
			assertTrue(String.format("Account type %s should not be granted %s", actor, granted.getAuthority()), expected.contains(granted.getAuthority()));
		}
	}
}

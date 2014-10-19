package com.gagror.data.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.security.core.GrantedAuthority;

import com.gagror.data.EnumUnitTestSupport;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class AccountTypeUnitTest extends EnumUnitTestSupport<AccountType> {

	@RequiredArgsConstructor
	private enum ExpectedResults {
		STANDARD(AccountType.STANDARD, 0,
				Collections.<AccountType>emptyList(),
				Collections.<String>emptyList()),
		ADMIN(AccountType.ADMIN, 2,
				Arrays.asList(AccountType.STANDARD, AccountType.ADMIN),
				Arrays.asList(SecurityRoles.ROLE_ADMIN)),
		SYSTEM_OWNER(AccountType.SYSTEM_OWNER, 1,
				Arrays.asList(AccountType.STANDARD, AccountType.ADMIN),
				Arrays.asList(SecurityRoles.ROLE_ADMIN));

		private final AccountType accountType;
		private final Integer databaseId;
		private final List<AccountType> mayEdit;
		private final List<String> authorities;

		public static ExpectedResults forAccountType(final AccountType type) {
			for(final ExpectedResults exp : values()) {
				if(exp.accountType.equals(type)) {
					return exp;
				}
			}
			throw new IllegalArgumentException(String.format("Test has no expected results for %s", type));
		}
	}

	@SuppressWarnings("unused")
	private final String name;

	private final AccountType accountType;

	@Test
	public void getMayEdit() {
		assertMayEdit(accountType, ExpectedResults.forAccountType(accountType).mayEdit);
	}

	@Test
	public void getAuthorities() {
		assertAuthorities(accountType, ExpectedResults.forAccountType(accountType).authorities);
	}

	@Test
	public void getId() {
		assertEquals(String.format("ID of account type %s is used in database and may not change", accountType),
				ExpectedResults.forAccountType(accountType).databaseId,
				accountType.getId());
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findInstances() {
		return parameterizeForInstances(AccountType.class);
	}

	private void assertMayEdit(final AccountType actor, final List<AccountType> expectedMayEdit) {
		for(final AccountType mayEdit : expectedMayEdit) {
			assertTrue(String.format("Account type %s should be able to edit %s", actor, mayEdit), actor.getMayEdit().contains(mayEdit));
		}
		for(final AccountType mayEdit : actor.getMayEdit()) {
			assertTrue(String.format("Account type %s should not be able to edit %s", actor, mayEdit), expectedMayEdit.contains(mayEdit));
		}
	}

	private void assertAuthorities(final AccountType actor, final List<String> expectedRoles) {
		for(final String exp : expectedRoles) {
			boolean found = false;
			for(final GrantedAuthority granted : actor.getAuthorities()) {
				if(exp.equals(granted.getAuthority())) {
					found = true;
				}
			}
			assertTrue(String.format("Account type %s should be granted %s", actor, exp), found);
		}
		for(final GrantedAuthority granted : actor.getAuthorities()) {
			assertTrue(String.format("Account type %s should not be granted %s", actor, granted.getAuthority()), expectedRoles.contains(granted.getAuthority()));
		}
	}
}

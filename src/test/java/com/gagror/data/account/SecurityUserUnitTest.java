package com.gagror.data.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

@RunWith(MockitoJUnitRunner.class)
public class SecurityUserUnitTest {

	private static final String USERNAME = "TheUsername";
	private static final String PASSWORD = "ThePassword";

	@Mock
	AccountEntity account;

	@Test
	public void active_notLocked() {
		securityFlagsTest(true, false);
	}

	@Test
	public void active_locked() {
		securityFlagsTest(true, true);
	}

	@Test
	public void notActive_notLocked() {
		securityFlagsTest(false, false);
	}

	@Test
	public void notActive_locked() {
		securityFlagsTest(false, true);
	}

	private void securityFlagsTest(final boolean active, final boolean locked) {
		when(account.isActive()).thenReturn(active);
		when(account.isLocked()).thenReturn(locked);
		final UserDetails instance = new SecurityUser(account);
		assertEquals("Wrong username", USERNAME, instance.getUsername());
		assertEquals("Wrong password", PASSWORD, instance.getPassword());
		assertEquals("Unexpected enabled flag", active, instance.isEnabled());
		assertEquals("Unexpected nonlocked flag", !locked, instance.isAccountNonLocked());
		// The concept of expiring accounts and credentials is not supported
		assertEquals("Unexpected account expired flag", true, instance.isAccountNonExpired());
		assertEquals("Unexpected credentials expired flag", true, instance.isCredentialsNonExpired());
	}

	@Before
	public void setupAccount() {
		when(account.getUsername()).thenReturn(USERNAME);
		when(account.getPassword()).thenReturn(PASSWORD);
		when(account.isActive()).thenReturn(true);
		when(account.isLocked()).thenReturn(false);
		when(account.getAccountType()).thenReturn(AccountType.STANDARD);
	}
}

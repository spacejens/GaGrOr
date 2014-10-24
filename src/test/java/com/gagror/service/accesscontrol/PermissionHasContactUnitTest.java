package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.ContactEntity;

@RunWith(MockitoJUnitRunner.class)
public class PermissionHasContactUnitTest {

	private static final Long FIRST_CONTACT_ID = 12L;
	private static final Long SECOND_CONTACT_ID = 34L;
	private static final Long UNKNOWN_CONTACT_ID = 56L;

	PermissionHasContact instance;

	@Mock
	AccountEntity account;

	@Mock
	ContactEntity firstContact;

	@Mock
	ContactEntity secondContact;

	@Test
	public void parseId() {
		assertEquals("Unexpected parse result", Long.valueOf(712L), instance.parseId("712"));
	}

	@Test
	public void hasPermission_ok() {
		assertTrue("Contact should be present", instance.hasPermission(FIRST_CONTACT_ID, account));
	}

	@Test
	public void hasPermission_contactNotFound() {
		assertFalse("Contact should not be present", instance.hasPermission(UNKNOWN_CONTACT_ID, account));
	}

	@Before
	public void setupContacts() {
		when(firstContact.getId()).thenReturn(FIRST_CONTACT_ID);
		when(secondContact.getId()).thenReturn(SECOND_CONTACT_ID);
	}

	@Before
	public void setupAccount() {
		final Set<ContactEntity> contacts = new HashSet<>();
		contacts.add(firstContact);
		contacts.add(secondContact);
		when(account.getContacts()).thenReturn(contacts);
	}

	@Before
	public void setupInstance() {
		instance = new PermissionHasContact();
	}
}

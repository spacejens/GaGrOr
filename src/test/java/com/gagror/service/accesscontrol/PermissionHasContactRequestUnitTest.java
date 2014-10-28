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
import com.gagror.data.account.ContactType;

@RunWith(MockitoJUnitRunner.class)
public class PermissionHasContactRequestUnitTest {

	private static final Long APPROVED_CONTACT_ID = 12L;
	private static final Long REQUESTED_CONTACT_ID = 34L;
	private static final Long UNKNOWN_CONTACT_ID = 56L;

	PermissionHasContactRequest instance;

	@Mock
	AccountEntity account;

	@Mock
	ContactEntity approvedContact;

	@Mock
	ContactEntity requestedContact;

	@Test
	public void parseId() {
		assertEquals("Unexpected parse result", Long.valueOf(712L), instance.parseId("712"));
	}

	@Test
	public void hasPermission_ok() {
		assertTrue("Permission to requested contact should succeed", instance.hasPermission(REQUESTED_CONTACT_ID, account));
	}

	@Test
	public void hasPermission_alreadyApproved() {
		assertFalse("Permission to already approved contact should fail", instance.hasPermission(APPROVED_CONTACT_ID, account));
	}

	@Test
	public void hasPermission_unknown() {
		assertFalse("Permission to unknown contact should fail", instance.hasPermission(UNKNOWN_CONTACT_ID, account));
	}

	@Before
	public void setupAccount() {
		// Set IDs of contacts before attempting to add them to the set
		when(approvedContact.getId()).thenReturn(APPROVED_CONTACT_ID);
		when(approvedContact.getContactType()).thenReturn(ContactType.APPROVED);
		when(requestedContact.getId()).thenReturn(REQUESTED_CONTACT_ID);
		when(requestedContact.getContactType()).thenReturn(ContactType.REQUESTED);
		// Create the incoming contact set
		final Set<ContactEntity> contacts = new HashSet<>();
		contacts.add(approvedContact);
		contacts.add(requestedContact);
		when(account.getIncomingContacts()).thenReturn(contacts);
	}

	@Before
	public void setupInstance() {
		instance = new PermissionHasContactRequest();
	}
}

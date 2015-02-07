package com.gagror.service.social;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.ContactEntity;
import com.gagror.data.account.ContactType;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupInviteInput;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupMemberRepository;
import com.gagror.data.group.GroupMembershipChangeException;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.MemberType;
import com.gagror.service.accesscontrol.AccessControlService;

@RunWith(MockitoJUnitRunner.class)
public class InviteGroupPersisterUnitTest {

	private static final Long GROUP_ID = 11L;
	private static final Long UNKNOWN_GROUP_ID = 634782L;
	private static final Long ACCOUNT_ID_REQUEST = 123L;
	private static final Long ACCOUNT_ID_CONTACT = 456L;
	private static final Long CONTACT_ID = 789L;

	InviteGroupPersister instance;

	@Mock
	AccessControlService accessControlService;

	@Mock
	GroupRepository groupRepository;

	@Mock
	AccountRepository accountRepository;

	@Mock
	GroupMemberRepository groupMemberRepository;

	@Mock
	GroupInviteInput groupInviteForm;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupEntity group;

	@Mock
	AccountEntity requestAccount;

	@Mock
	AccountEntity contactAccount;

	@Mock
	ContactEntity contact;

	@Test
	public void sendInvitations_ok() {
		groupInviteForm.getSelected().add(ACCOUNT_ID_CONTACT);
		final boolean result = instance.save(groupInviteForm, bindingResult);
		assertTrue("Saving should have succeeded", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<GroupMemberEntity> member = ArgumentCaptor.forClass(GroupMemberEntity.class);
		verify(groupMemberRepository).persist(member.capture());
		assertSame("Wrong group", group, member.getValue().getGroup());
		assertSame("Wrong account", contactAccount, member.getValue().getAccount());
		assertEquals("Wrong member type", MemberType.INVITED, member.getValue().getMemberType());
		assertTrue("Group should have member", group.getGroupMemberships().contains(member.getValue()));
		assertTrue("Account should have member", contactAccount.getGroupMemberships().contains(member.getValue()));
	}

	@Test(expected=DataNotFoundException.class)
	public void sendInvitations_groupNotFound() {
		when(groupInviteForm.getId()).thenReturn(UNKNOWN_GROUP_ID);
		instance.save(groupInviteForm, bindingResult);
	}

	@Test
	public void sendInvitations_inviteMultipleUsers() {
		final long anotherAccountID = 476593L;
		final AccountEntity anotherAccount = mock(AccountEntity.class);
		mockAccount(anotherAccount, anotherAccountID);
		final ContactEntity anotherContact = mock(ContactEntity.class);
		mockContact(anotherContact, 57697L, requestAccount, anotherAccount, ContactType.APPROVED);
		groupInviteForm.getSelected().add(ACCOUNT_ID_CONTACT);
		groupInviteForm.getSelected().add(anotherAccountID);
		final boolean result = instance.save(groupInviteForm, bindingResult);
		assertTrue("Saving should have succeeded", result);
		verify(groupMemberRepository, times(2)).persist(any(GroupMemberEntity.class));
	}

	@Test(expected=GroupMembershipChangeException.class)
	public void sendInvitations_userNotFound() {
		groupInviteForm.getSelected().add(74697834L);
		instance.save(groupInviteForm, bindingResult);
	}

	@Test
	public void sendInvitations_alreadyMember() {
		final Long id = 34675L;
		final GroupMemberEntity groupMember = mock(GroupMemberEntity.class);
		mockGroupMember(groupMember, group, id, MemberType.MEMBER, contactAccount);
		groupInviteForm.getSelected().add(ACCOUNT_ID_CONTACT);
		final boolean result = instance.save(groupInviteForm, bindingResult);
		assertTrue("Saving should have succeeded", result);
		verify(groupMemberRepository, never()).persist(any(GroupMemberEntity.class));
	}

	@Test(expected=GroupMembershipChangeException.class)
	public void sendInvitations_notContact() {
		final long anotherAccountID = 476593L;
		final AccountEntity anotherAccount = mock(AccountEntity.class);
		mockAccount(anotherAccount, anotherAccountID);
		groupInviteForm.getSelected().add(anotherAccountID);
		instance.save(groupInviteForm, bindingResult);
	}

	private void mockAccount(final AccountEntity account, final Long id) {
		when(account.getId()).thenReturn(id);
		when(accountRepository.load(id)).thenReturn(account);
		final Set<ContactEntity> contacts = new HashSet<>();
		when(account.getContacts()).thenReturn(contacts);
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		when(account.getGroupMemberships()).thenReturn(memberships);
	}

	private void mockContact(final ContactEntity contact, final Long id, final AccountEntity owner, final AccountEntity other, final ContactType contactType) {
		when(contact.getId()).thenReturn(id);
		when(contact.getOwner()).thenReturn(owner);
		when(contact.getContact()).thenReturn(other);
		when(contact.getContactType()).thenReturn(contactType);
		owner.getContacts().add(contact);
	}

	private void mockGroupMember(final GroupMemberEntity member, final GroupEntity group, final Long id, final MemberType memberType, final AccountEntity account) {
		when(member.getId()).thenReturn(id);
		when(member.getGroup()).thenReturn(group);
		when(member.getAccount()).thenReturn(account);
		when(member.getMemberType()).thenReturn(memberType);
		group.getGroupMemberships().add(member);
		account.getGroupMemberships().add(member);
	}

	@Before
	public void setupAccounts() {
		mockAccount(requestAccount, ACCOUNT_ID_REQUEST);
		mockAccount(contactAccount, ACCOUNT_ID_CONTACT);
		mockContact(contact, CONTACT_ID, requestAccount, contactAccount, ContactType.APPROVED);
		when(accessControlService.getRequestAccountEntity()).thenReturn(requestAccount);
	}

	@Before
	public void setupGroup() {
		when(group.getId()).thenReturn(GROUP_ID);
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		when(group.getGroupMemberships()).thenReturn(memberships);
		when(groupRepository.load(GROUP_ID)).thenReturn(group);
		doThrow(DataNotFoundException.class).when(groupRepository).load(UNKNOWN_GROUP_ID);
	}

	@Before
	public void setupGroupInviteForm() {
		when(groupInviteForm.getId()).thenReturn(GROUP_ID);
		final Set<Long> invited = new HashSet<>();
		when(groupInviteForm.getSelected()).thenReturn(invited);
	}

	@Before
	public void setupInstance() {
		instance = new InviteGroupPersister();
		instance.accessControlService = accessControlService;
		instance.accountRepository = accountRepository;
		instance.groupRepository = groupRepository;
		instance.groupMemberRepository = groupMemberRepository;
	}
}

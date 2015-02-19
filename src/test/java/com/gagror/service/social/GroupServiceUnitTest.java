package com.gagror.service.social;

import static com.gagror.GagrorAssert.assertIds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.EchoAnswer;
import com.gagror.data.DataNotFoundException;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.ContactEntity;
import com.gagror.data.account.ContactType;
import com.gagror.data.group.GroupEditOutput;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupMemberRepository;
import com.gagror.data.group.GroupMembershipChangeException;
import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.MemberType;
import com.gagror.data.group.NotGroupMemberException;
import com.gagror.service.accesscontrol.AccessControlService;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceUnitTest {

	private static final Long FIRST_GROUP_ID = 11L;
	private static final Long SECOND_GROUP_ID = 22L;
	private static final Long THIRD_GROUP_ID = 33L;
	private static final Long FOURTH_GROUP_ID = 44L;
	private static final Long ANOTHER_GROUP_ID = 55L;
	private static final Long UNKNOWN_GROUP_ID = 754328L;
	private static final String FIRST_GROUP_NAME = "First";
	private static final String SECOND_GROUP_NAME = "Second";
	private static final String THIRD_GROUP_NAME = "Third";
	private static final String FOURTH_GROUP_NAME = "Fourth";
	private static final String ANOTHER_GROUP_NAME = "Another";
	private static final Long FIRST_MEMBERSHIP_ID = 111L;
	private static final Long SECOND_MEMBERSHIP_ID = 222L;
	private static final Long THIRD_MEMBERSHIP_ID = 333L;
	private static final Long FOURTH_MEMBERSHIP_ID = 444L;

	private static final Long ACCOUNT_ID_REQUEST = 123L;
	private static final Long ACCOUNT_ID_CONTACT = 456L;
	private static final Long CONTACT_ID = 789L;

	GroupService instance;

	@Mock
	AccessControlService accessControlService;

	@Mock
	GroupRepository groupRepository;

	@Mock
	GroupMemberRepository groupMemberRepository;

	@Mock
	AccountRepository accountRepository;

	@Mock
	AccountService accountService;

	@Mock
	AccountEntity requestAccount;

	@Mock
	GroupMemberEntity firstGroupOwner;

	@Mock
	GroupMemberEntity secondGroupMember;

	@Mock
	GroupMemberEntity thirdGroupInvited;

	@Mock
	GroupMemberEntity fourthGroupInvited;

	@Mock
	GroupEntity firstGroup;

	@Mock
	GroupEntity secondGroup;

	@Mock
	GroupEntity thirdGroup;

	@Mock
	GroupEntity fourthGroup;

	@Mock
	GroupEntity anotherGroup;

	@Mock
	AccountEntity contactAccount;

	@Mock
	ContactEntity contact;

	@Test
	public void loadGroupList_ok() {
		Long[] expectedGroupIds = { FIRST_GROUP_ID, SECOND_GROUP_ID };
		assertIds(instance.loadGroupList(), expectedGroupIds);
	}

	@Test
	public void loadGroupList_noMemberships() {
		requestAccount.getGroupMemberships().clear();
		Long[] expectedGroupIds = {};
		assertIds(instance.loadGroupList(), expectedGroupIds);
	}

	@Test
	public void loadInvitationsList_ok() {
		Long[] expectedGroupIds = { FOURTH_GROUP_ID, THIRD_GROUP_ID };
		assertIds(instance.loadInvitationsList(), expectedGroupIds);
	}

	@Test
	public void loadInvitationsList_noInvitations() {
		requestAccount.getGroupMemberships().clear();
		Long[] expectedGroupIds = {};
		assertIds(instance.loadInvitationsList(), expectedGroupIds);
	}

	@Test
	public void loadPublicGroupList_ok() {
		Long[] expectedGroupIds = { FIRST_GROUP_ID, FOURTH_GROUP_ID, SECOND_GROUP_ID, THIRD_GROUP_ID };
		assertIds(instance.loadPublicGroupList(), expectedGroupIds);
	}

	@Test
	public void loadPublicGroupList_noPublicGroups() {
		final List<GroupEntity> noGroups = new ArrayList<>();
		when(groupRepository.listViewableByAnyone()).thenReturn(noGroups);
		Long[] expectedGroupIds = {};
		assertIds(instance.loadPublicGroupList(), expectedGroupIds);
	}

	@Test
	public void viewGroup_owner() {
		viewGroup_ok(FIRST_GROUP_ID, FIRST_GROUP_NAME, MemberType.OWNER);
	}

	@Test
	public void viewGroup_member() {
		viewGroup_ok(SECOND_GROUP_ID, SECOND_GROUP_NAME, MemberType.MEMBER);
	}

	@Test
	public void viewGroup_invited() {
		viewGroup_ok(THIRD_GROUP_ID, THIRD_GROUP_NAME, MemberType.INVITED);
	}

	@Test
	public void viewGroup_notMember() {
		viewGroup_ok(ANOTHER_GROUP_ID, ANOTHER_GROUP_NAME, null);
	}

	private void viewGroup_ok(final Long id, final String expectedName, final MemberType expectedMemberType) {
		final GroupReferenceOutput result = instance.viewGroup(id);
		assertEquals("Wrong group found", expectedName, result.getName());
		assertEquals("Wrong membership type", expectedMemberType, result.getMemberType());
	}

	@Test(expected=DataNotFoundException.class)
	public void viewGroup_notFound() {
		instance.viewGroup(UNKNOWN_GROUP_ID);
	}

	@Test
	public void editGroup_ok() {
		final GroupEditOutput result = instance.editGroup(FIRST_GROUP_ID);
		assertEquals("Wrong group found", FIRST_GROUP_NAME, result.getName());
	}

	@Test(expected=NotGroupMemberException.class)
	public void editGroup_notMember() {
		instance.editGroup(37598345L);
	}

	@Test
	public void viewGroupMembers_owner() {
		viewGroupMembers_ok(FIRST_GROUP_ID, FIRST_GROUP_NAME, MemberType.OWNER);
	}

	@Test
	public void viewGroupMembers_member() {
		viewGroupMembers_ok(SECOND_GROUP_ID, SECOND_GROUP_NAME, MemberType.MEMBER);
	}

	@Test
	public void viewGroupMembers_invited() {
		viewGroupMembers_ok(THIRD_GROUP_ID, THIRD_GROUP_NAME, MemberType.INVITED);
	}

	private void viewGroupMembers_ok(final Long id, final String expectedName, final MemberType expectedMemberType) {
		final GroupReferenceOutput result = instance.viewGroupMembers(id);
		assertEquals("Wrong group found", expectedName, result.getName());
		assertEquals("Wrong membership type", expectedMemberType, result.getMemberType());
	}

	@Test
	public void viewGroupMembers_notMember() {
		viewGroupMembers_ok(ANOTHER_GROUP_ID, ANOTHER_GROUP_NAME, null);
	}

	@Test(expected=DataNotFoundException.class)
	public void viewGroupMembers_notFound() {
		instance.viewGroupMembers(UNKNOWN_GROUP_ID);
	}

	@Test
	public void loadPossibleUsersToInvite_ok() {
		final List<AccountReferenceOutput> result = instance.loadPossibleUsersToInvite(FIRST_GROUP_ID);
		assertEquals("Wrong number of candidates loaded", 1, result.size());
		assertEquals("Wrong candidate loaded", ACCOUNT_ID_CONTACT, result.iterator().next().getId());
	}

	@Test
	public void loadPossibleUsersToInvite_sorted() {
		final AccountEntity anotherContactAccount = mock(AccountEntity.class);
		mockAccount(anotherContactAccount, 56736L);
		when(anotherContactAccount.getName()).thenReturn("AAAA");
		when(contactAccount.getName()).thenReturn("BBBB");
		final ContactEntity anotherContact = mock(ContactEntity.class);
		mockContact(anotherContact, 39999L, requestAccount, anotherContactAccount, ContactType.APPROVED);
		final List<AccountReferenceOutput> result = instance.loadPossibleUsersToInvite(FIRST_GROUP_ID);
		assertEquals("Wrong number of candidates loaded", 2, result.size());
		assertEquals("Wrong candidate first", anotherContactAccount.getId(), result.get(0).getId());
		assertEquals("Wrong candidate second", contactAccount.getId(), result.get(1).getId());
	}

	@Test
	public void loadPossibleUsersToInvite_contactNotAccepted() {
		when(contact.getContactType()).thenReturn(ContactType.REQUESTED);
		final List<AccountReferenceOutput> result = instance.loadPossibleUsersToInvite(FIRST_GROUP_ID);
		assertTrue("Candidate that is not an accepted contact should not have been loaded", result.isEmpty());
	}

	@Test
	public void loadPossibleUsersToInvite_alreadyMember() {
		final Long id = 34675L;
		final GroupMemberEntity groupMember = mock(GroupMemberEntity.class);
		mockGroupMember(groupMember, firstGroup, id, MemberType.MEMBER, contactAccount);
		final List<AccountReferenceOutput> result = instance.loadPossibleUsersToInvite(FIRST_GROUP_ID);
		assertTrue("Candidate that is already member should not have been loaded", result.isEmpty());
		verify(groupMember, never()).getMemberType(); // Member type shouldn't matter
	}

	@Test
	public void accept_ok() {
		// Add a non-contact account that is a member of the group
		final AccountEntity nonContactAccount = mock(AccountEntity.class);
		mockAccount(nonContactAccount, 3475689L);
		final GroupMemberEntity nonContactMember = mock(GroupMemberEntity.class);
		mockGroupMember(nonContactMember, thirdGroup, 235442L, MemberType.MEMBER, nonContactAccount);
		// A contact account is also a member of the group
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, thirdGroup, 4634667L, MemberType.MEMBER, contactAccount);
		// A requested contact is also a member of the group
		final AccountEntity requestedContactAccount = mock(AccountEntity.class);
		mockAccount(requestedContactAccount, 6342541L);
		final ContactEntity requestedContact = mock(ContactEntity.class);
		mockContact(requestedContact, 23523L, requestAccount, requestedContactAccount, ContactType.REQUESTED);
		final GroupMemberEntity requestedContactMember = mock(GroupMemberEntity.class);
		mockGroupMember(requestedContactMember, thirdGroup, 3465346L, MemberType.MEMBER, requestedContactAccount);
		// Another non-contact account is also invited to the group
		final AccountEntity invitedAccount = mock(AccountEntity.class);
		mockAccount(invitedAccount, 3475689L);
		final GroupMemberEntity invitedMember = mock(GroupMemberEntity.class);
		mockGroupMember(invitedMember, thirdGroup, 4563457L, MemberType.INVITED, invitedAccount);
		// An incoming contact request is also a member of the group
		final AccountEntity incomingRequestedContactAccount = mock(AccountEntity.class);
		mockAccount(incomingRequestedContactAccount, 34634567L);
		final ContactEntity incomingRequestedContact = mock(ContactEntity.class);
		mockContact(incomingRequestedContact, 634626272L, incomingRequestedContactAccount, requestAccount, ContactType.REQUESTED);
		final GroupMemberEntity incomingRequestedContactMember = mock(GroupMemberEntity.class);
		mockGroupMember(incomingRequestedContactMember, thirdGroup, 463456634L, MemberType.MEMBER, incomingRequestedContactAccount);
		// Set up a dummy contact to be returned by contact creation (and used for verification of mirroring)
		final ContactEntity toBeMirrored = mock(ContactEntity.class);
		when(accountService.createContact(requestAccount, ContactType.AUTOMATIC, nonContactAccount)).thenReturn(toBeMirrored);
		// Accept the invitation and verify that non-contact members (but not invited users) are added as contacts (and mirrored)
		instance.accept(THIRD_MEMBERSHIP_ID);
		verify(thirdGroupInvited).setMemberType(MemberType.MEMBER);
		verify(requestedContact).setContactType(ContactType.AUTOMATIC);
		verify(incomingRequestedContact).setContactType(ContactType.AUTOMATIC);
		verify(accountService).createContact(requestAccount, ContactType.AUTOMATIC, nonContactAccount);
		verify(accountService).mirrorContact(toBeMirrored);
		verify(accountService).mirrorContact(requestedContact);
		verify(accountService).mirrorContact(incomingRequestedContact);
		verifyNoMoreInteractions(accountService);
	}

	@Test
	public void accept_alreadyMember() {
		instance.accept(SECOND_MEMBERSHIP_ID);
		// Silently ignored, which seems good because the invitation has already been accepted
		verify(secondGroupMember, never()).setMemberType(any(MemberType.class));
	}

	@Test
	public void accept_invitationNotFound() {
		instance.accept(74569793L);
		// Silently ignored. Maybe not ideal, but showing an error accomplishes very little
	}

	@Test
	public void decline_ok() {
		instance.decline(THIRD_MEMBERSHIP_ID);
		verify(groupMemberRepository).delete(thirdGroupInvited);
		assertFalse("Declining invitation should remove it from group", secondGroup.getGroupMemberships().contains(thirdGroupInvited));
		assertFalse("Declining invitation should remove it from account", requestAccount.getGroupMemberships().contains(thirdGroupInvited));
	}

	@Test
	public void decline_alreadyMember() {
		instance.decline(SECOND_MEMBERSHIP_ID);
		// Silently ignored. Maybe not ideal, but it is a weird case
		verify(groupMemberRepository, never()).delete(secondGroupMember);
		assertTrue("Attempting to decline already accepted membership should not remove it from group", secondGroup.getGroupMemberships().contains(secondGroupMember));
		assertTrue("Attempting to decline already accepted membership should not remove it from account", requestAccount.getGroupMemberships().contains(secondGroupMember));
	}

	@Test
	public void decline_invitationNotFound() {
		instance.decline(5789345L);
		// Silently ignored, which seems good because the invitation no longer exists
	}

	@Test
	public void leave_member() {
		instance.leave(SECOND_GROUP_ID);
		verify(groupMemberRepository).delete(secondGroupMember);
		assertFalse("Account should no longer have member", requestAccount.getGroupMemberships().contains(secondGroupMember));
		assertFalse("Group should no longer have member", secondGroup.getGroupMemberships().contains(secondGroupMember));
	}

	@Test(expected=GroupMembershipChangeException.class)
	public void leave_onlyOwner() {
		instance.leave(FIRST_GROUP_ID);
	}

	@Test
	public void leave_severalOwners() {
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, firstGroup, 5467936L, MemberType.OWNER, contactAccount);
		instance.leave(FIRST_GROUP_ID);
		verify(groupMemberRepository).delete(firstGroupOwner);
		assertFalse("Account should no longer have member", requestAccount.getGroupMemberships().contains(firstGroupOwner));
		assertFalse("Group should no longer have member", firstGroup.getGroupMemberships().contains(firstGroupOwner));
	}

	@Test
	public void leave_notMember() {
		instance.leave(346566535L);
		// Silently ignored, which seems good because the user doesn't want to be a member anyway
	}

	@Test
	public void promote_member() {
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, firstGroup, 5467936L, MemberType.MEMBER, contactAccount);
		instance.promote(FIRST_GROUP_ID, ACCOUNT_ID_CONTACT);
		verify(contactMember).setMemberType(MemberType.OWNER);
	}

	@Test
	public void promote_owner() {
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, firstGroup, 5467936L, MemberType.OWNER, contactAccount);
		instance.promote(FIRST_GROUP_ID, ACCOUNT_ID_CONTACT);
		verify(contactMember).setMemberType(MemberType.OWNER);
		// Silently ignored, which seems good because we want the user to be an owner
	}

	@Test(expected=GroupMembershipChangeException.class)
	public void promote_invited() {
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, firstGroup, 5467936L, MemberType.INVITED, contactAccount);
		instance.promote(FIRST_GROUP_ID, ACCOUNT_ID_CONTACT);
	}

	@Test(expected=GroupMembershipChangeException.class)
	public void promote_self() {
		instance.promote(FIRST_GROUP_ID, ACCOUNT_ID_REQUEST);
	}

	@Test(expected=NotGroupMemberException.class)
	public void promote_groupNotFound() {
		instance.promote(348967346L, ACCOUNT_ID_CONTACT);
	}

	@Test(expected=GroupMembershipChangeException.class)
	public void promote_accountNotMember() {
		instance.promote(FIRST_GROUP_ID, 4563458L);
	}

	@Test
	public void demote_member() {
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, firstGroup, 5467936L, MemberType.MEMBER, contactAccount);
		instance.demote(FIRST_GROUP_ID, ACCOUNT_ID_CONTACT);
		verify(contactMember).setMemberType(MemberType.MEMBER);
		// Silently ignored, which seems good because we want the user to be a member
	}

	@Test
	public void demote_owner() {
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, firstGroup, 5467936L, MemberType.OWNER, contactAccount);
		instance.demote(FIRST_GROUP_ID, ACCOUNT_ID_CONTACT);
		verify(contactMember).setMemberType(MemberType.MEMBER);
	}

	@Test(expected=GroupMembershipChangeException.class)
	public void demote_invited() {
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, firstGroup, 5467936L, MemberType.INVITED, contactAccount);
		instance.demote(FIRST_GROUP_ID, ACCOUNT_ID_CONTACT);
	}

	@Test(expected=GroupMembershipChangeException.class)
	public void demote_self() {
		instance.demote(FIRST_GROUP_ID, ACCOUNT_ID_REQUEST);
	}

	@Test(expected=NotGroupMemberException.class)
	public void demote_groupNotFound() {
		instance.demote(348967346L, ACCOUNT_ID_CONTACT);
	}

	@Test(expected=GroupMembershipChangeException.class)
	public void demote_accountNotMember() {
		instance.demote(FIRST_GROUP_ID, 4563458L);
	}

	@Test
	public void remove_member() {
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, firstGroup, 5467936L, MemberType.MEMBER, contactAccount);
		instance.remove(FIRST_GROUP_ID, ACCOUNT_ID_CONTACT);
		verify(groupMemberRepository).delete(contactMember);
		assertFalse("Account should no longer have member", contactAccount.getGroupMemberships().contains(contactMember));
		assertFalse("Group should no longer have member", firstGroup.getGroupMemberships().contains(contactMember));
	}

	@Test
	public void remove_owner() {
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, firstGroup, 5467936L, MemberType.OWNER, contactAccount);
		instance.remove(FIRST_GROUP_ID, ACCOUNT_ID_CONTACT);
		verify(groupMemberRepository).delete(contactMember);
		assertFalse("Account should no longer have member", contactAccount.getGroupMemberships().contains(contactMember));
		assertFalse("Group should no longer have member", firstGroup.getGroupMemberships().contains(contactMember));
	}

	@Test
	public void remove_invited() {
		final GroupMemberEntity contactMember = mock(GroupMemberEntity.class);
		mockGroupMember(contactMember, firstGroup, 5467936L, MemberType.INVITED, contactAccount);
		instance.remove(FIRST_GROUP_ID, ACCOUNT_ID_CONTACT);
		verify(groupMemberRepository).delete(contactMember);
		assertFalse("Account should no longer have member", contactAccount.getGroupMemberships().contains(contactMember));
		assertFalse("Group should no longer have member", firstGroup.getGroupMemberships().contains(contactMember));
	}

	@Test(expected=GroupMembershipChangeException.class)
	public void remove_self() {
		instance.remove(FIRST_GROUP_ID, ACCOUNT_ID_REQUEST);
	}

	@Test(expected=NotGroupMemberException.class)
	public void remove_groupNotFound() {
		instance.remove(348967346L, ACCOUNT_ID_CONTACT);
	}

	@Test
	public void remove_accountNotFound() {
		instance.remove(FIRST_GROUP_ID, 4563458L);
		verify(groupMemberRepository, never()).delete(any(GroupMemberEntity.class));
		// Silently ignored, which seems good because we don't want the user to be a member
	}

	@Before
	public void setupGroupRepository() {
		when(groupRepository.persist(any(GroupEntity.class))).thenAnswer(new EchoAnswer<>());
		when(groupRepository.listViewableByAnyone()).thenReturn(Arrays.asList(firstGroup, secondGroup, thirdGroup, fourthGroup));
		doThrow(DataNotFoundException.class).when(groupRepository).load(UNKNOWN_GROUP_ID);
	}

	@Before
	public void setupGroups() {
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		when(requestAccount.getGroupMemberships()).thenReturn(memberships);
		mockGroup(firstGroup, FIRST_GROUP_ID, FIRST_GROUP_NAME);
		mockGroupMember(firstGroupOwner, firstGroup, FIRST_MEMBERSHIP_ID, MemberType.OWNER, requestAccount);
		mockGroup(secondGroup, SECOND_GROUP_ID, SECOND_GROUP_NAME);
		mockGroupMember(secondGroupMember, secondGroup, SECOND_MEMBERSHIP_ID, MemberType.MEMBER, requestAccount);
		mockGroup(thirdGroup, THIRD_GROUP_ID, THIRD_GROUP_NAME);
		mockGroupMember(thirdGroupInvited, thirdGroup, THIRD_MEMBERSHIP_ID, MemberType.INVITED, requestAccount);
		mockGroup(fourthGroup, FOURTH_GROUP_ID, FOURTH_GROUP_NAME);
		mockGroupMember(fourthGroupInvited, fourthGroup, FOURTH_MEMBERSHIP_ID, MemberType.INVITED, requestAccount);
		mockGroup(anotherGroup, ANOTHER_GROUP_ID, ANOTHER_GROUP_NAME);
	}

	private void mockGroup(final GroupEntity group, final Long id, final String name) {
		when(group.getId()).thenReturn(id);
		when(group.getName()).thenReturn(name);
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		when(group.getGroupMemberships()).thenReturn(memberships);
		when(groupRepository.load(id)).thenReturn(group);
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
	}

	private void mockAccount(final AccountEntity account, final Long id) {
		when(account.getId()).thenReturn(id);
		when(accountRepository.load(id)).thenReturn(account);
		final Set<ContactEntity> contacts = new HashSet<>();
		when(account.getContacts()).thenReturn(contacts);
		final Set<ContactEntity> incomingContacts = new HashSet<>();
		when(account.getIncomingContacts()).thenReturn(incomingContacts);
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		when(account.getGroupMemberships()).thenReturn(memberships);
	}

	private void mockContact(final ContactEntity contact, final Long id, final AccountEntity owner, final AccountEntity other, final ContactType contactType) {
		when(contact.getId()).thenReturn(id);
		when(contact.getOwner()).thenReturn(owner);
		when(contact.getContact()).thenReturn(other);
		when(contact.getContactType()).thenReturn(contactType);
		owner.getContacts().add(contact);
		other.getIncomingContacts().add(contact);
	}

	@Before
	public void setupAccessControlService() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(requestAccount);
	}

	@Before
	public void setupInstance() {
		instance = new GroupService();
		instance.accessControlService = accessControlService;
		instance.groupRepository = groupRepository;
		instance.groupMemberRepository = groupMemberRepository;
		instance.accountRepository = accountRepository;
		instance.accountService = accountService;
	}
}

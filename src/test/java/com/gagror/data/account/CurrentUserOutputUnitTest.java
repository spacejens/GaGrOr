package com.gagror.data.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.group.MemberType;
import com.gagror.data.group.PlayerOwnedOutput;

@RunWith(MockitoJUnitRunner.class)
public class CurrentUserOutputUnitTest {

	private static final Long REQUEST_ACCOUNT_ID = 1L;
	private static final Long OTHER_ACCOUNT_ID = 2L;

	private static final Long GROUP_ID_OWNER = 3L;
	private static final Long GROUP_ID_MEMBER = 4L;
	private static final Long GROUP_ID_INVITED = 5L;
	private static final Long GROUP_ID_NOT_MEMBER = 6L;

	private static final AccountType REQUEST_ACCOUNT_TYPE = AccountType.ADMIN;
	private static final EnumSet<AccountType> REQUEST_ACCOUNT_MAY_EDIT = EnumSet.copyOf(REQUEST_ACCOUNT_TYPE.getMayEdit());
	private static final EnumSet<AccountType> REQUEST_ACCOUNT_MAY_NOT_EDIT = EnumSet.complementOf(REQUEST_ACCOUNT_MAY_EDIT);

	CurrentUserOutput instance;

	@Mock
	AccountReferenceOutput otherAccount;

	@Mock
	GroupReferenceOutput groupOwner;

	@Mock
	GroupReferenceOutput groupMember;

	@Mock
	GroupReferenceOutput groupInvited;

	@Mock
	GroupReferenceOutput groupNotMember;

	@Mock
	PlayerOwnedOutput data;

	@Test
	public void is_loggedIn_requestAccount() {
		final AccountReferenceOutput requestAccount = whenLoggedIn();
		assertTrue(instance.is(requestAccount));
	}

	@Test
	public void is_loggedIn_otherAccount() {
		whenLoggedIn();
		assertFalse(instance.is(otherAccount));
	}

	@Test
	public void is_notLoggedIn_otherAccount() {
		whenNotLoggedIn();
		assertFalse(instance.is(otherAccount));
	}

	@Test
	public void isNot_loggedIn_requestAccount() {
		final AccountReferenceOutput requestAccount = whenLoggedIn();
		assertFalse(instance.isNot(requestAccount));
	}

	@Test
	public void isNot_loggedIn_otherAccount() {
		whenLoggedIn();
		assertTrue(instance.isNot(otherAccount));
	}

	@Test
	public void isNot_notLoggedIn_otherAccount() {
		whenNotLoggedIn();
		assertTrue(instance.isNot(otherAccount));
	}

	@Test
	public void getMayEdit_loggedIn() {
		whenLoggedIn();
		assertEquals(REQUEST_ACCOUNT_TYPE.getMayEdit(), instance.getMayEdit());
	}

	@Test
	public void getMayEdit_notLoggedIn() {
		whenNotLoggedIn();
		assertEquals(Collections.emptyList(), instance.getMayEdit());
	}

	@Test
	public void mayEdit_loggedIn_ok() {
		whenLoggedIn();
		for(final AccountType accountType : REQUEST_ACCOUNT_MAY_EDIT) {
			when(otherAccount.getAccountType()).thenReturn(accountType);
			assertTrue(String.format("Should be able to edit %s", accountType), instance.mayEdit(otherAccount));
		}
	}

	@Test
	public void mayEdit_loggedIn_ownAccount_ok() {
		final AccountReferenceOutput requestAccount = whenLoggedIn();
		assertTrue("Should be able to edit own account", instance.mayEdit(requestAccount));
	}

	@Test
	public void mayEdit_loggedIn_notOk() {
		whenLoggedIn();
		for(final AccountType accountType : REQUEST_ACCOUNT_MAY_NOT_EDIT) {
			when(otherAccount.getAccountType()).thenReturn(accountType);
			assertFalse(String.format("Should not be able to edit %s", accountType), instance.mayEdit(otherAccount));
		}
	}

	@Test
	public void mayEdit_notLoggedIn_notOk() {
		whenNotLoggedIn();
		for(final AccountType accountType : AccountType.values()) {
			when(otherAccount.getAccountType()).thenReturn(accountType);
			assertFalse(String.format("Should not be able to edit %s", accountType), instance.mayEdit(otherAccount));
		}
	}

	@Test
	public void getMemberType_loggedIn_owner() {
		whenLoggedIn();
		assertEquals(MemberType.OWNER, instance.getMemberType(groupOwner));
	}

	@Test
	public void getMemberType_loggedIn_member() {
		whenLoggedIn();
		assertEquals(MemberType.MEMBER, instance.getMemberType(groupMember));
	}

	@Test
	public void getMemberType_loggedIn_invited() {
		whenLoggedIn();
		assertEquals(MemberType.INVITED, instance.getMemberType(groupInvited));
	}

	@Test
	public void getMemberType_loggedIn_notMember() {
		whenLoggedIn();
		assertNull(instance.getMemberType(groupNotMember));
	}

	@Test
	public void getMemberType_notLoggedIn_notMember() {
		whenNotLoggedIn();
		assertNull(instance.getMemberType(groupNotMember));
	}

	@Test
	public void isInvitedOrMember_loggedIn_owner() {
		whenLoggedIn();
		assertTrue(instance.isInvitedOrMember(groupOwner));
	}

	@Test
	public void isInvitedOrMember_loggedIn_member() {
		whenLoggedIn();
		assertTrue(instance.isInvitedOrMember(groupMember));
	}

	@Test
	public void isInvitedOrMember_loggedIn_invited() {
		whenLoggedIn();
		assertTrue(instance.isInvitedOrMember(groupInvited));
	}

	@Test
	public void isInvitedOrMember_loggedIn_notMember() {
		whenLoggedIn();
		assertFalse(instance.isInvitedOrMember(groupNotMember));
	}

	@Test
	public void isInvitedOrMember_notLoggedIn_notMember() {
		whenNotLoggedIn();
		assertFalse(instance.isInvitedOrMember(groupNotMember));
	}

	@Test
	public void isInvited_loggedIn_owner() {
		whenLoggedIn();
		assertFalse(instance.isInvited(groupOwner));
	}

	@Test
	public void isInvited_loggedIn_member() {
		whenLoggedIn();
		assertFalse(instance.isInvited(groupMember));
	}

	@Test
	public void isInvited_loggedIn_invited() {
		whenLoggedIn();
		assertTrue(instance.isInvited(groupInvited));
	}

	@Test
	public void isInvited_loggedIn_notMember() {
		whenLoggedIn();
		assertFalse(instance.isInvited(groupNotMember));
	}

	@Test
	public void isInvited_notLoggedIn_notMember() {
		whenNotLoggedIn();
		assertFalse(instance.isInvited(groupNotMember));
	}

	@Test
	public void isMember_loggedIn_owner() {
		whenLoggedIn();
		assertTrue(instance.isMember(groupOwner));
	}

	@Test
	public void isMember_loggedIn_member() {
		whenLoggedIn();
		assertTrue(instance.isMember(groupMember));
	}

	@Test
	public void isMember_loggedIn_invited() {
		whenLoggedIn();
		assertFalse(instance.isMember(groupInvited));
	}

	@Test
	public void isMember_loggedIn_notMember() {
		whenLoggedIn();
		assertFalse(instance.isMember(groupNotMember));
	}

	@Test
	public void isMember_notLoggedIn_notMember() {
		whenNotLoggedIn();
		assertFalse(instance.isMember(groupNotMember));
	}

	@Test
	public void isOwner_loggedIn_owner() {
		whenLoggedIn();
		assertTrue(instance.isOwner(groupOwner));
	}

	@Test
	public void isOwner_loggedIn_member() {
		whenLoggedIn();
		assertFalse(instance.isOwner(groupMember));
	}

	@Test
	public void isOwner_loggedIn_invited() {
		whenLoggedIn();
		assertFalse(instance.isOwner(groupInvited));
	}

	@Test
	public void isOwner_loggedIn_notMember() {
		whenLoggedIn();
		assertFalse(instance.isOwner(groupNotMember));
	}

	@Test
	public void isOwner_notLoggedIn_notMember() {
		whenNotLoggedIn();
		assertFalse(instance.isOwner(groupNotMember));
	}

	@Test
	public void canActAsPlayer_loggedIn_player_owner() {
		final AccountReferenceOutput requestAccount = whenLoggedIn();
		when(data.getPlayer()).thenReturn(requestAccount);
		when(data.getGroup()).thenReturn(groupOwner);
		assertTrue(instance.canActAsPlayer(data));
	}

	@Test
	public void canActAsPlayer_loggedIn_player_member() {
		final AccountReferenceOutput requestAccount = whenLoggedIn();
		when(data.getPlayer()).thenReturn(requestAccount);
		when(data.getGroup()).thenReturn(groupMember);
		assertTrue(instance.canActAsPlayer(data));
	}

	@Test
	public void canActAsPlayer_loggedIn_player_invited() {
		final AccountReferenceOutput requestAccount = whenLoggedIn();
		when(data.getPlayer()).thenReturn(requestAccount);
		when(data.getGroup()).thenReturn(groupInvited);
		assertFalse(instance.canActAsPlayer(data));
	}

	@Test
	public void canActAsPlayer_loggedIn_player_notMember() {
		final AccountReferenceOutput requestAccount = whenLoggedIn();
		when(data.getPlayer()).thenReturn(requestAccount);
		when(data.getGroup()).thenReturn(groupNotMember);
		assertFalse(instance.canActAsPlayer(data));
	}

	@Test
	public void canActAsPlayer_loggedIn_notPlayer_owner() {
		whenLoggedIn();
		when(data.getPlayer()).thenReturn(otherAccount);
		when(data.getGroup()).thenReturn(groupOwner);
		assertTrue(instance.canActAsPlayer(data));
	}

	@Test
	public void canActAsPlayer_loggedIn_notPlayer_member() {
		whenLoggedIn();
		when(data.getPlayer()).thenReturn(otherAccount);
		when(data.getGroup()).thenReturn(groupMember);
		assertFalse(instance.canActAsPlayer(data));
	}

	@Test
	public void canActAsPlayer_loggedIn_notPlayer_invited() {
		whenLoggedIn();
		when(data.getPlayer()).thenReturn(otherAccount);
		when(data.getGroup()).thenReturn(groupInvited);
		assertFalse(instance.canActAsPlayer(data));
	}

	@Test
	public void canActAsPlayer_loggedIn_notPlayer_notMember() {
		whenLoggedIn();
		when(data.getPlayer()).thenReturn(otherAccount);
		when(data.getGroup()).thenReturn(groupNotMember);
		assertFalse(instance.canActAsPlayer(data));
	}

	@Test
	public void canActAsPlayer_notLoggedIn() {
		whenNotLoggedIn();
		when(data.getPlayer()).thenReturn(otherAccount);
		when(data.getGroup()).thenReturn(groupNotMember);
		assertFalse(instance.canActAsPlayer(data));
	}

	@Test
	public void isLoggedIn_loggedIn() {
		whenLoggedIn();
		assertTrue(instance.isLoggedIn());
	}

	@Test
	public void isLoggedIn_notLoggedIn() {
		whenNotLoggedIn();
		assertFalse(instance.isLoggedIn());
	}

	@Test
	public void sanityCheck_mayEdit() {
		assertFalse("Test setup is wrong, should be able to edit at least one account type", REQUEST_ACCOUNT_MAY_EDIT.isEmpty());
	}

	@Test
	public void sanityCheck_mayNotEdit() {
		assertFalse("Test setup is wrong, should not be able to edit at least one account type", REQUEST_ACCOUNT_MAY_NOT_EDIT.isEmpty());
	}

	private AccountReferenceOutput whenLoggedIn() {
		final AccountEntity requestAccountEntity = mock(AccountEntity.class);
		when(requestAccountEntity.getId()).thenReturn(REQUEST_ACCOUNT_ID);
		when(requestAccountEntity.getAccountType()).thenReturn(REQUEST_ACCOUNT_TYPE);
		when(requestAccountEntity.getGroupMemberships()).thenReturn(new HashSet<GroupMemberEntity>());
		requestAccountEntity.getGroupMemberships().add(mockMembership(GROUP_ID_OWNER, MemberType.OWNER));
		requestAccountEntity.getGroupMemberships().add(mockMembership(GROUP_ID_MEMBER, MemberType.MEMBER));
		requestAccountEntity.getGroupMemberships().add(mockMembership(GROUP_ID_INVITED, MemberType.INVITED));
		// Create the instance
		instance = new CurrentUserOutput(requestAccountEntity);
		// Create and return an account reference to the request account for tests that need it
		final AccountReferenceOutput requestAccount = mock(AccountReferenceOutput.class);
		when(requestAccount.getId()).thenReturn(REQUEST_ACCOUNT_ID);
		return requestAccount;
	}

	private GroupMemberEntity mockMembership(final Long groupId, final MemberType memberType) {
		final GroupEntity group = mock(GroupEntity.class);
		when(group.getId()).thenReturn(groupId);
		final GroupMemberEntity membership = mock(GroupMemberEntity.class);
		when(membership.getId()).thenReturn(groupId); // Just need a unique ID to avoid equals collision
		when(membership.getGroup()).thenReturn(group);
		when(membership.getMemberType()).thenReturn(memberType);
		return membership;
	}

	private void whenNotLoggedIn() {
		instance = new CurrentUserOutput();
	}

	@Before
	public void setup() {
		when(otherAccount.getId()).thenReturn(OTHER_ACCOUNT_ID);
		when(groupOwner.getId()).thenReturn(GROUP_ID_OWNER);
		when(groupMember.getId()).thenReturn(GROUP_ID_MEMBER);
		when(groupInvited.getId()).thenReturn(GROUP_ID_INVITED);
		when(groupNotMember.getId()).thenReturn(GROUP_ID_NOT_MEMBER);
	}
}

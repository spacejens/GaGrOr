package com.gagror.data.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountReferenceOutput;

@RunWith(MockitoJUnitRunner.class)
public class GroupViewOutputUnitTest {

	private static final Date DATE_MEMBER = new Date();
	private static final Date DATE_GROUP = new Date(0);

	private static final String USERNAME_FIRST_OWNER = "First owner";
	private static final String USERNAME_SECOND_OWNER = "Second owner";
	private static final String USERNAME_FIRST_MEMBER = "First member";
	private static final String USERNAME_SECOND_MEMBER = "Second member";
	private static final String USERNAME_FIRST_INVITED = "First invited";
	private static final String USERNAME_SECOND_INVITED = "Second invited";

	@Mock
	private GroupEntity group;

	@Mock
	private GroupMemberEntity memberFirstOwner;

	@Mock
	private AccountEntity accountFirstOwner;

	@Mock
	private GroupMemberEntity memberSecondOwner;

	@Mock
	private AccountEntity accountSecondOwner;

	@Mock
	private GroupMemberEntity memberFirstMember;

	@Mock
	private AccountEntity accountFirstMember;

	@Mock
	private GroupMemberEntity memberSecondMember;

	@Mock
	private AccountEntity accountSecondMember;

	@Mock
	private GroupMemberEntity memberFirstInvited;

	@Mock
	private AccountEntity accountFirstInvited;

	@Mock
	private GroupMemberEntity memberSecondInvited;

	@Mock
	private AccountEntity accountSecondInvited;

	@Test
	public void getCreated_fromGroup() {
		final GroupViewOutput instance = new GroupViewOutput(group);
		assertEquals("Created date should be from group", DATE_GROUP, instance.getCreated());
	}

	@Test
	public void getCreated_fromMembership() {
		final GroupViewOutput instance = new GroupViewOutput(memberFirstOwner);
		assertEquals("Created date should be from group", DATE_GROUP, instance.getCreated());
	}

	@Test
	public void getOwners_fromMembership() {
		final GroupViewOutput instance = new GroupViewOutput(memberFirstOwner);
		assertUsers(instance.getOwners(), USERNAME_FIRST_OWNER, USERNAME_SECOND_OWNER);
	}

	@Test
	public void getMembers_fromMembership() {
		final GroupViewOutput instance = new GroupViewOutput(memberFirstOwner);
		assertUsers(instance.getMembers(), USERNAME_FIRST_MEMBER, USERNAME_SECOND_MEMBER);
	}

	@Test
	public void getInvited_fromMembership() {
		final GroupViewOutput instance = new GroupViewOutput(memberFirstOwner);
		assertUsers(instance.getInvited(), USERNAME_FIRST_INVITED, USERNAME_SECOND_INVITED);
	}

	@Test
	public void getOwners_emptyWhenNotMember() {
		final GroupViewOutput instance = new GroupViewOutput(group);
		assertTrue("Should not list owners when not member of group", instance.getOwners().isEmpty());
	}

	@Test
	public void getMembers_emptyWhenNotMember() {
		final GroupViewOutput instance = new GroupViewOutput(group);
		assertTrue("Should not list members when not member of group", instance.getMembers().isEmpty());
	}

	@Test
	public void getInvited_emptyWhenNotMember() {
		final GroupViewOutput instance = new GroupViewOutput(group);
		assertTrue("Should not list invited when not member of group", instance.getInvited().isEmpty());
	}

	private void assertUsers(final List<AccountReferenceOutput> accounts, final String... expectedUsernames) {
		final List<String> expected = Arrays.asList(expectedUsernames);
		final List<String> actual = new ArrayList<>(accounts.size());
		for(final AccountReferenceOutput account : accounts) {
			actual.add(account.getUsername());
		}
		assertEquals("Wrong usernames returned", expected, actual);
	}

	@Before
	public void setup() {
		setupGroup();
		setupAccount(accountFirstOwner, USERNAME_FIRST_OWNER);
		setupMembership(memberFirstOwner, accountFirstOwner, MemberType.OWNER);
		setupAccount(accountSecondOwner, USERNAME_SECOND_OWNER);
		setupMembership(memberSecondOwner, accountSecondOwner, MemberType.OWNER);
		setupAccount(accountFirstMember, USERNAME_FIRST_MEMBER);
		setupMembership(memberFirstMember, accountFirstMember, MemberType.MEMBER);
		setupAccount(accountSecondMember, USERNAME_SECOND_MEMBER);
		setupMembership(memberSecondMember, accountSecondMember, MemberType.MEMBER);
		setupAccount(accountFirstInvited, USERNAME_FIRST_INVITED);
		setupMembership(memberFirstInvited, accountFirstInvited, MemberType.INVITED);
		setupAccount(accountSecondInvited, USERNAME_SECOND_INVITED);
		setupMembership(memberSecondInvited, accountSecondInvited, MemberType.INVITED);
	}

	/** Incrementing counter used to give every different entity a unique ID */
	private long id = 1L;

	private void setupGroup() {
		when(group.getId()).thenReturn(id++);
		when(group.getCreated()).thenReturn(DATE_GROUP);
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		when(group.getGroupMemberships()).thenReturn(memberships);
	}

	private void setupAccount(final AccountEntity account, final String username) {
		when(account.getId()).thenReturn(id++);
		when(account.getUsername()).thenReturn(username);
	}

	private void setupMembership(final GroupMemberEntity member, final AccountEntity account, final MemberType memberType) {
		when(member.getId()).thenReturn(id++);
		when(member.getGroup()).thenReturn(group);
		when(member.getAccount()).thenReturn(account);
		when(member.getMemberType()).thenReturn(memberType);
		when(member.getCreated()).thenReturn(DATE_MEMBER);
		group.getGroupMemberships().add(member);
	}
}
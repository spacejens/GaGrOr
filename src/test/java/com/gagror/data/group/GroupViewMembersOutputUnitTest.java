package com.gagror.data.group;

import static com.gagror.GagrorAssert.assertNames;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.account.AccountEntity;

@RunWith(MockitoJUnitRunner.class)
public class GroupViewMembersOutputUnitTest {

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
	public void getCreated_ok() {
		final GroupViewMembersOutput instance = new GroupViewMembersOutput(group);
		assertEquals("Created date should be from group", DATE_GROUP, instance.getCreated());
	}

	@Test
	public void getMembers() {
		final GroupViewMembersOutput instance = new GroupViewMembersOutput(group);
		assertNames(instance.getMembers(), USERNAME_FIRST_INVITED, USERNAME_FIRST_MEMBER, USERNAME_FIRST_OWNER, USERNAME_SECOND_INVITED, USERNAME_SECOND_MEMBER, USERNAME_SECOND_OWNER);
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
		when(account.getName()).thenReturn(username);
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

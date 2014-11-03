package com.gagror.service.accesscontrol;

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
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.MemberType;

@RunWith(MockitoJUnitRunner.class)
public class PermissionAdminGroupUnitTest {

	private static final Long FIRST_GROUP_ID = 123L;
	private static final Long SECOND_GROUP_ID = 456L;
	private static final Long OTHER_GROUP_ID = 999L;

	private static final Long FIRST_MEMBER_ID = 123L;
	private static final Long SECOND_MEMBER_ID = 456L;

	PermissionAdminGroup instance;

	@Mock
	AccountEntity account;

	@Mock
	GroupMemberEntity firstGroupOwner;

	@Mock
	GroupEntity firstGroup;

	@Mock
	GroupMemberEntity secondGroupMember;

	@Mock
	GroupEntity secondGroup;

	@Test
	public void hasPermission_member() {
		assertTrue("Group owner should have permission", instance.hasPermission(FIRST_GROUP_ID, account));
	}

	@Test
	public void hasPermission_invited() {
		assertFalse("Group member should not have permission", instance.hasPermission(SECOND_GROUP_ID, account));
	}

	@Test
	public void hasPermission_unknownGroup() {
		assertFalse("Should not have permission to unknown group", instance.hasPermission(OTHER_GROUP_ID, account));
	}

	@Before
	public void setupAccount() {
		when(firstGroup.getId()).thenReturn(FIRST_GROUP_ID);
		when(firstGroupOwner.getId()).thenReturn(FIRST_MEMBER_ID);
		when(firstGroupOwner.getMemberType()).thenReturn(MemberType.OWNER);
		when(firstGroupOwner.getGroup()).thenReturn(firstGroup);
		when(secondGroup.getId()).thenReturn(SECOND_GROUP_ID);
		when(secondGroupMember.getId()).thenReturn(SECOND_MEMBER_ID);
		when(secondGroupMember.getMemberType()).thenReturn(MemberType.MEMBER);
		when(secondGroupMember.getGroup()).thenReturn(secondGroup);
		final Set<GroupMemberEntity> memberships = new HashSet<>();
		memberships.add(firstGroupOwner);
		memberships.add(secondGroupMember);
		when(account.getGroupMemberships()).thenReturn(memberships);
	}

	@Before
	public void setupInstance() {
		instance = new PermissionAdminGroup();
	}
}

package com.gagror.data.group;

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

@RunWith(MockitoJUnitRunner.class)
public class GroupMemberOutputUnitTest {

	@Mock
	GroupMemberEntity memberInput;

	@Mock
	AccountEntity accountInput;

	@Mock
	GroupMemberEntity memberAnother;

	@Mock
	AccountEntity accountAnother;

	@Mock
	GroupEntity group;

	@Test
	public void isOnlyOwner_singleOwner() {
		assertTrue("Should be noted as single owner", new GroupMemberOutput(memberInput).isOnlyOwner());
	}

	@Test
	public void isOnlyOwner_multipleOwners() {
		when(memberAnother.getMemberType()).thenReturn(MemberType.OWNER);
		assertFalse("Should not be noted as single owner when there are other owners", new GroupMemberOutput(memberInput).isOnlyOwner());
	}

	@Test
	public void isOnlyOwner_notOwner() {
		when(memberInput.getMemberType()).thenReturn(MemberType.MEMBER);
		when(memberAnother.getMemberType()).thenReturn(MemberType.OWNER);
		assertFalse("Should not be noted as single owner when not owner", new GroupMemberOutput(memberInput).isOnlyOwner());
	}

	@Before
	public void setup() {
		long id = 1;
		when(memberInput.getId()).thenReturn(id++);
		when(memberInput.getGroup()).thenReturn(group);
		when(memberInput.getMemberType()).thenReturn(MemberType.OWNER);
		when(memberInput.getAccount()).thenReturn(accountInput);
		when(memberAnother.getId()).thenReturn(id++);
		when(memberAnother.getGroup()).thenReturn(group);
		when(memberAnother.getMemberType()).thenReturn(MemberType.MEMBER);
		when(memberAnother.getAccount()).thenReturn(accountAnother);
		final Set<GroupMemberEntity> members = new HashSet<>();
		members.add(memberInput);
		members.add(memberAnother);
		when(group.getGroupMemberships()).thenReturn(members);
	}
}

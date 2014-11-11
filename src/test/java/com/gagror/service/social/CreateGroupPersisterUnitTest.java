package com.gagror.service.social;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupCreateInput;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupMemberRepository;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.MemberType;
import com.gagror.service.accesscontrol.AccessControlService;

@RunWith(MockitoJUnitRunner.class)
public class CreateGroupPersisterUnitTest {

	private static final String NEW_GROUP_NAME = "New group";

	CreateGroupPersister instance;

	@Mock
	AccessControlService accessControlService;

	@Mock
	GroupRepository groupRepository;

	@Mock
	GroupMemberRepository groupMemberRepository;

	@Mock
	AccountEntity requestAccount;

	@Mock
	GroupCreateInput groupCreateForm;

	@Mock
	BindingResult bindingResult;

	@Test
	public void save_ok() {
		final boolean result = instance.save(groupCreateForm, bindingResult);
		assertTrue("Saving should have been successful", result);
		final ArgumentCaptor<GroupEntity> group = ArgumentCaptor.forClass(GroupEntity.class);
		verify(groupRepository).save(group.capture());
		assertEquals("Wrong name of created group", NEW_GROUP_NAME, group.getValue().getName());
		final ArgumentCaptor<GroupMemberEntity> groupMember = ArgumentCaptor.forClass(GroupMemberEntity.class);
		verify(groupMemberRepository).save(groupMember.capture());
		assertSame("Group member should be for saved group", group.getValue(), groupMember.getValue().getGroup());
		assertSame("Group member should be for request account", requestAccount, groupMember.getValue().getAccount());
		assertEquals("Group member should be owner", MemberType.OWNER, groupMember.getValue().getMemberType());
		assertTrue("Group should have member", group.getValue().getGroupMemberships().contains(groupMember.getValue()));
		assertTrue("Account should be member", requestAccount.getGroupMemberships().contains(groupMember.getValue()));
	}

	@Before
	public void setupForm() {
		when(groupCreateForm.getName()).thenReturn(NEW_GROUP_NAME);
	}

	@Before
	public void setupRequestAccount() {
		when(accessControlService.getRequestAccountEntity()).thenReturn(requestAccount);
		when(requestAccount.getGroupMemberships()).thenReturn(new HashSet<GroupMemberEntity>());
	}

	@Before
	public void setupAccountRepository() {
		when(groupRepository.save(any(GroupEntity.class))).thenAnswer(new Answer<GroupEntity>() {
			@Override
			public GroupEntity answer(final InvocationOnMock invocation) throws Throwable {
				return (GroupEntity)invocation.getArguments()[0];
			}
		});
	}

	@Before
	public void setupInstance() {
		instance = new CreateGroupPersister();
		instance.accessControlService = accessControlService;
		instance.groupRepository = groupRepository;
		instance.groupMemberRepository = groupMemberRepository;
	}
}

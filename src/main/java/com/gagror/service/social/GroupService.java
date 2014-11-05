package com.gagror.service.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.ContactEntity;
import com.gagror.data.group.GroupCreateInput;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupInviteInput;
import com.gagror.data.group.GroupListOutput;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupMemberRepository;
import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.GroupViewMembersOutput;
import com.gagror.data.group.MemberType;
import com.gagror.service.accesscontrol.AccessControlService;

@Service
@Transactional
@CommonsLog
public class GroupService {

	@Autowired
	AccessControlService accessControlService;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	GroupMemberRepository groupMemberRepository;

	public List<GroupListOutput> loadGroupList() {
		log.debug("Loading group list");
		final List<GroupListOutput> output = new ArrayList<>();
		for(final GroupMemberEntity member : accessControlService.getRequestAccountEntity().getGroupMemberships()) {
			if(member.getMemberType().isMember()) {
				output.add(new GroupListOutput(member));
			}
		}
		Collections.sort(output);
		return output;
	}

	public List<GroupListOutput> loadInvitationsList() {
		log.debug("Loading invitations list");
		final List<GroupListOutput> output = new ArrayList<>();
		for(final GroupMemberEntity member : accessControlService.getRequestAccountEntity().getGroupMemberships()) {
			if(member.getMemberType().isInvitation()) {
				output.add(new GroupListOutput(member));
			}
		}
		Collections.sort(output);
		return output;
	}

	public void createGroup(final GroupCreateInput groupCreateForm, final BindingResult bindingResult) {
		// Verify that the group can be created
		// NOTE: Currently there are no rules preventing this
		if(bindingResult.hasErrors()) {
			return;
		}
		log.info(String.format("Creating group '%s'", groupCreateForm.getName()));
		// Create the group
		final GroupEntity group = groupRepository.save(new GroupEntity(groupCreateForm.getName()));
		final GroupMemberEntity owner = new GroupMemberEntity(
				group,
				accessControlService.getRequestAccountEntity(),
				MemberType.OWNER);
		groupMemberRepository.save(owner);
	}

	public GroupReferenceOutput viewGroup(final Long groupId) {
		final GroupEntity group = loadGroup(groupId);
		log.debug(String.format("Loaded group %s for viewing", group));
		final AccountEntity currentUser = accessControlService.getRequestAccountEntity();
		for(final GroupMemberEntity membership : group.getGroupMemberships()) {
			if(membership.getAccount().equals(currentUser)) {
				return new GroupReferenceOutput(membership);
			}
		}
		return new GroupReferenceOutput(group);
	}

	public GroupViewMembersOutput viewGroupMembers(final Long groupId) {
		final GroupEntity group = loadGroup(groupId);
		log.debug(String.format("Loaded group %s for viewing", group));
		final AccountEntity currentUser = accessControlService.getRequestAccountEntity();
		for(final GroupMemberEntity membership : group.getGroupMemberships()) {
			if(membership.getAccount().equals(currentUser)) {
				return new GroupViewMembersOutput(membership);
			}
		}
		throw new IllegalArgumentException(String.format("Could not load members for group %d, request account is not a member", groupId));
	}

	private GroupEntity loadGroup(final Long groupId) {
		final GroupEntity group = groupRepository.findOne(groupId);
		if(null == group) {
			throw new IllegalArgumentException(String.format("Failed to load group %d", groupId));
		}
		return group;
	}

	public List<AccountReferenceOutput> loadPossibleUsersToInvite(final Long groupId) {
		final GroupEntity group = loadGroup(groupId);
		// Find the group of users who are already invited or members
		final Set<AccountEntity> groupMemberAccounts = findGroupMemberAccounts(group);
		// Find the possible users
		final List<AccountReferenceOutput> output = new ArrayList<>();
		for(final ContactEntity contact : accessControlService.getRequestAccountEntity().getContacts()) {
			// Filter out group members, and contacts who have not been accepted
			if(! groupMemberAccounts.contains(contact.getContact()) && contact.getContactType().isContact()) {
				output.add(new AccountReferenceOutput(contact.getContact()));
			}
		}
		Collections.sort(output);
		return output;
	}

	private Set<AccountEntity> findGroupMemberAccounts(final GroupEntity group) {
		final Set<AccountEntity> groupMemberAccounts = new HashSet<>();
		for(final GroupMemberEntity membership : group.getGroupMemberships()) {
			groupMemberAccounts.add(membership.getAccount());
		}
		return groupMemberAccounts;
	}

	public void sendInvitations(final GroupInviteInput groupInviteForm, final BindingResult bindingResult) {
		final GroupEntity group = loadGroup(groupInviteForm.getId());
		final Set<AccountEntity> groupMemberAccounts = findGroupMemberAccounts(group);
		for(final Long invited : groupInviteForm.getSelected()) {
			// TODO If invited user is not a contact, fail or ignore?
			final AccountEntity account = accountRepository.findById(invited);
			if(null == account) {
				throw new IllegalArgumentException(String.format("Failed to load invited account %d", invited));
			}
			if(! groupMemberAccounts.contains(account)) {
				groupMemberRepository.save(new GroupMemberEntity(
						group,
						account,
						MemberType.INVITED));
			}
		}
	}
}

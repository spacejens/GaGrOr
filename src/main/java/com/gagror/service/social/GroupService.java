package com.gagror.service.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.gagror.data.group.GroupCreateInput;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupListOutput;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupMemberRepository;
import com.gagror.data.group.GroupRepository;
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
}

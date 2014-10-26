package com.gagror.service.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.group.GroupListOutput;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.service.accesscontrol.AccessControlService;

@Service
@Transactional
@CommonsLog
public class GroupService {

	@Autowired
	AccessControlService accessControlService;

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
}

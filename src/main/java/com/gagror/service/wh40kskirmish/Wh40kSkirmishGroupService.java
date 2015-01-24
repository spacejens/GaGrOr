package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishGroupOutput;
import com.gagror.service.social.GroupService;

@Service
@Transactional
@CommonsLog
public class Wh40kSkirmishGroupService {

	@Autowired
	GroupService groupService;

	public Wh40kSkirmishGroupOutput viewGroup(final Long groupId) {
		final GroupEntity group = groupService.loadGroup(groupId);
		log.debug(String.format("Loaded group %s for viewing", group));
		final GroupMemberEntity membership = groupService.findGroupMemberForRequestAccount(group);
		if(null != membership) {
			return new Wh40kSkirmishGroupOutput(membership);
		}
		return new Wh40kSkirmishGroupOutput(group);
	}
}

package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.wh40kskirmish.GroupOutput;
import com.gagror.service.social.GroupService;

@Service
@Transactional
@CommonsLog
public class Wh40kSkirmishService {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	GroupService groupService;

	public GroupOutput viewGroup(final Long groupId) {
		final GroupEntity group = groupRepository.load(groupId);
		log.debug(String.format("Loaded group %s for viewing", group));
		return new GroupOutput(group);
	}
}

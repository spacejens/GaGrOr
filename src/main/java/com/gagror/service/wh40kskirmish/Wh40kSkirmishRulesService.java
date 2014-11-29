package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishRulesGangTypesOutput;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishRulesOutput;
import com.gagror.service.social.GroupService;

@Service
@Transactional
@CommonsLog
public class Wh40kSkirmishRulesService {

	@Autowired
	GroupService groupService;

	public Wh40kSkirmishRulesOutput viewRules(final Long groupId) {
		log.debug(String.format("Viewing rules for group %d", groupId));
		return new Wh40kSkirmishRulesOutput(loadRules(groupId));
	}

	public Wh40kSkirmishRulesGangTypesOutput viewRulesWithGangTypes(final Long groupId) {
		log.debug(String.format("Listing gang types for group %d", groupId));
		return new Wh40kSkirmishRulesGangTypesOutput(loadRules(groupId));
	}

	private Wh40kSkirmishRulesEntity loadRules(final Long groupId) {
		final GroupEntity group = groupService.loadGroup(groupId);
		if(null == group.getWh40kSkirmishRules()) {
			throw new IllegalArgumentException(String.format("Group %s does not have WH40K skirmish rules", group));
		}
		return group.getWh40kSkirmishRules();
	}
}

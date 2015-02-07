package com.gagror.data.wh40kskirmish.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;

@Repository
public class RulesRepository {

	@Autowired
	GroupRepository groupRepository;

	public Wh40kSkirmishRulesEntity load(final Long groupId) {
		final GroupEntity group = groupRepository.load(groupId);
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}
}

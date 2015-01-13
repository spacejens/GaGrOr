package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishFactionEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishFactionOutput;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishGangTypeListChildrenOutput;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishGangTypeOutput;
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

	private Wh40kSkirmishGangTypeEntity loadGangType(final Long groupId, final Long gangTypeId) {
		final Wh40kSkirmishRulesEntity rules = loadRules(groupId);
		for(final Wh40kSkirmishGangTypeEntity gangType : rules.getGangTypes()) {
			if(gangType.getId().equals(gangTypeId)) {
				return gangType;
			}
		}
		throw new IllegalArgumentException(String.format("Failed to find gang type %d in group %d", gangTypeId, groupId));
	}

	public Wh40kSkirmishGangTypeOutput viewGangType(final Long groupId, final Long gangTypeId) {
		return new Wh40kSkirmishGangTypeOutput(loadGangType(groupId, gangTypeId));
	}

	public Wh40kSkirmishGangTypeListChildrenOutput viewGangTypeListChildren(final Long groupId, final Long gangTypeId) {
		return new Wh40kSkirmishGangTypeListChildrenOutput(loadGangType(groupId, gangTypeId));
	}

	private Wh40kSkirmishFactionEntity loadFaction(final Long groupId, final Long gangTypeId, final Long factionId) {
		final Wh40kSkirmishGangTypeEntity gangType = loadGangType(groupId, gangTypeId);
		for(final Wh40kSkirmishFactionEntity faction : gangType.getFactions()) {
			if(faction.getId().equals(factionId)) {
				return faction;
			}
		}
		throw new IllegalArgumentException(String.format("Failed to find faction %d in gang type %d of group %d",
				factionId, gangTypeId, groupId));
	}

	public Wh40kSkirmishFactionOutput viewFaction(final Long groupId, final Long gangTypeId, final Long factionId) {
		return new Wh40kSkirmishFactionOutput(loadFaction(groupId, gangTypeId, factionId));
	}
}

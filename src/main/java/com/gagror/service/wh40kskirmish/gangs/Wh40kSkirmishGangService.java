package com.gagror.service.wh40kskirmish.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.gangs.Wh40kSkirmishGangEntity;
import com.gagror.data.wh40kskirmish.gangs.Wh40kSkirmishGangOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionEntity;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishRulesService;

@Service
@Transactional
public class Wh40kSkirmishGangService {

	@Autowired
	Wh40kSkirmishRulesService rulesService;

	public Wh40kSkirmishGangOutput viewGang(
			final Long groupId, final Long gangTypeId, final Long factionId, final Long gangId) {
		return new Wh40kSkirmishGangOutput(
				loadGang(groupId, gangTypeId, factionId, gangId),
				rulesService.viewRules(groupId));
	}

	private Wh40kSkirmishGangEntity loadGang(
			final Long groupId, final Long gangTypeId, final Long factionId, final Long gangId) {
		final Wh40kSkirmishFactionEntity faction = rulesService.loadFaction(groupId, gangTypeId, factionId);
		for(final Wh40kSkirmishGangEntity gang : faction.getGangs()) {
			if(gang.getId().equals(gangId)) {
				return gang;
			}
		}
		throw new DataNotFoundException(String.format("Gang %d (faction %d, gang type %d, group %d)",
				gangId, factionId, gangTypeId, groupId));
	}
}

package com.gagror.service.wh40kskirmish.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.service.wh40kskirmish.rules.RulesService;

@Service
@Transactional
public class GangService {

	@Autowired
	RulesService rulesService;

	public GangOutput viewGang(
			final Long groupId, final Long gangTypeId, final Long factionId, final Long gangId) {
		return new GangOutput(
				loadGang(groupId, gangTypeId, factionId, gangId),
				rulesService.viewRules(groupId));
	}

	private GangEntity loadGang(
			final Long groupId, final Long gangTypeId, final Long factionId, final Long gangId) {
		final FactionEntity faction = rulesService.loadFaction(groupId, gangTypeId, factionId);
		for(final GangEntity gang : faction.getGangs()) {
			if(gang.getId().equals(gangId)) {
				return gang;
			}
		}
		throw new DataNotFoundException(String.format("Gang %d (faction %d, gang type %d, group %d)",
				gangId, factionId, gangTypeId, groupId));
	}
}

package com.gagror.service.wh40kskirmish.gangs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupViewMembersOutput;
import com.gagror.data.wh40kskirmish.gangs.EditGangOutput;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangOutput;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionRepository;
import com.gagror.service.social.GroupService;
import com.gagror.service.wh40kskirmish.rules.RulesService;

@Service
@Transactional
public class GangService {

	@Autowired
	GroupService groupService;

	@Autowired
	RulesService rulesService;

	@Autowired
	FactionRepository factionRepository;

	public EditGangOutput prepareToCreateGang(final Long groupId) {
		final GroupViewMembersOutput group = groupService.viewGroupMembers(groupId);
		final RulesOutput rules = rulesService.viewRules(groupId);
		final List<FactionReferenceOutput> factions = rulesService.listAllFactions(groupId);
		return EditGangOutput.createGang(group, rules, factions);
	}

	public EditGangOutput prepareToEditGang(
			final Long groupId, final Long gangTypeId, final Long factionId, final Long gangId) {
		final GroupViewMembersOutput group = groupService.viewGroupMembers(groupId);
		final GangOutput gang = viewGang(groupId, gangTypeId, factionId, gangId);
		return EditGangOutput.editGang(group, gang);
	}

	public GangOutput viewGang(
			final Long groupId, final Long gangTypeId, final Long factionId, final Long gangId) {
		return new GangOutput(
				loadGang(groupId, gangTypeId, factionId, gangId),
				rulesService.viewRules(groupId));
	}

	private GangEntity loadGang(
			final Long groupId, final Long gangTypeId, final Long factionId, final Long gangId) {
		final FactionEntity faction = factionRepository.load(groupId, gangTypeId, factionId);
		for(final GangEntity gang : faction.getGangs()) {
			if(gang.hasId(gangId)) {
				return gang;
			}
		}
		throw new DataNotFoundException(String.format("Gang %d (faction %d, gang type %d, group %d)",
				gangId, factionId, gangTypeId, groupId));
	}
}

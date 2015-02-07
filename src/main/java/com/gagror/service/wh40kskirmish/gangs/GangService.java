package com.gagror.service.wh40kskirmish.gangs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.group.GroupViewMembersOutput;
import com.gagror.data.wh40kskirmish.gangs.EditGangOutput;
import com.gagror.data.wh40kskirmish.gangs.GangOutput;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;
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
	GangRepository gangRepository;

	public EditGangOutput prepareToCreateGang(final Long groupId) {
		final GroupViewMembersOutput group = groupService.viewGroupMembers(groupId);
		final RulesOutput rules = rulesService.viewRules(groupId);
		final List<FactionReferenceOutput> factions = rulesService.listAllFactions(groupId);
		return EditGangOutput.createGang(group, rules, factions);
	}

	public EditGangOutput prepareToEditGang(final Long groupId, final Long gangId) {
		final GroupViewMembersOutput group = groupService.viewGroupMembers(groupId);
		final GangOutput gang = viewGang(groupId, gangId);
		return EditGangOutput.editGang(group, gang);
	}

	public GangOutput viewGang(final Long groupId, final Long gangId) {
		return new GangOutput(
				gangRepository.load(groupId, gangId),
				rulesService.viewRules(groupId));
	}
}

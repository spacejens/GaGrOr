package com.gagror.service.wh40kskirmish.gangs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.group.GroupViewMembersOutput;
import com.gagror.data.wh40kskirmish.gangs.EditGangOutput;
import com.gagror.data.wh40kskirmish.gangs.FighterEditOutput;
import com.gagror.data.wh40kskirmish.gangs.FighterEntity;
import com.gagror.data.wh40kskirmish.gangs.FighterRepository;
import com.gagror.data.wh40kskirmish.gangs.FighterViewOutput;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangOutput;
import com.gagror.data.wh40kskirmish.gangs.GangRecruitOutput;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.data.wh40kskirmish.gangs.GangViewOutput;
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

	@Autowired
	FighterRepository fighterRepository;

	@Autowired
	FighterViewService fighterViewService;

	public EditGangOutput prepareToCreateGang(final Long groupId) {
		final GroupViewMembersOutput group = groupService.viewGroupMembers(groupId);
		final RulesOutput rules = rulesService.viewRules(groupId);
		final List<FactionReferenceOutput> factions = rulesService.listAllFactions(groupId);
		return EditGangOutput.createGang(group, rules, factions);
	}

	public EditGangOutput prepareToEditGang(final Long groupId, final Long gangId) {
		final GroupViewMembersOutput group = groupService.viewGroupMembers(groupId);
		return EditGangOutput.editGang(
				group,
				new GangOutput(gangRepository.load(groupId, gangId), rulesService.viewRules(groupId)));
	}

	public GangOutput viewGang(final Long groupId, final Long gangId) {
		final GangEntity gang = gangRepository.load(groupId, gangId);
		final List<FighterViewOutput> fighters = new ArrayList<>();
		for(final FighterEntity fighter : gang.getFighters()) {
			fighters.add(fighterViewService.view(fighter));
		}
		Collections.sort(fighters); // TODO Sort fighters in a more appropriate order when viewing gang (fixed positions?)
		return new GangViewOutput(
				gang,
				rulesService.viewRules(groupId),
				fighters);
	}

	public GangRecruitOutput prepareToRecruitFighter(final Long groupId, final Long gangId) {
		return new GangRecruitOutput(
				gangRepository.load(groupId, gangId),
				rulesService.viewRules(groupId));
	}

	public FighterViewOutput viewFighter(final Long groupId, final Long fighterId) {
		final FighterEntity fighter = fighterRepository.load(groupId, fighterId);
		final GangOutput gang = new GangOutput(fighter.getGang(), rulesService.viewRules(groupId));
		return fighterViewService.view(fighter, gang);
	}

	public FighterEditOutput editFighter(final Long groupId, final Long fighterId) {
		final FighterEntity fighter = fighterRepository.load(groupId, fighterId);
		return new FighterEditOutput(fighter);
	}
}

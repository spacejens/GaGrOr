package com.gagror.service.wh40kskirmish.gangs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupViewMembersOutput;
import com.gagror.data.wh40kskirmish.gangs.EditGangOutput;
import com.gagror.data.wh40kskirmish.gangs.FighterEntity;
import com.gagror.data.wh40kskirmish.gangs.FighterRepository;
import com.gagror.data.wh40kskirmish.gangs.FighterViewOutput;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangOutput;
import com.gagror.data.wh40kskirmish.gangs.GangRecruitOutput;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.service.social.GroupService;
import com.gagror.service.wh40kskirmish.rules.RulesService;

@RunWith(MockitoJUnitRunner.class)
public class GangServiceUnitTest {

	private static final Long GROUP_ID = 1L;
	private static final Long GANG_TYPE_ID = 2L;
	private static final Long FACTION_ID = 3L;
	private static final Long GANG_ID = 4L;
	private static final Long FIGHTER_ID = 5L;
	private static final String GANG_NAME = "Gang";
	private static final String FIGHTER_NAME = "Fighter";

	GangService instance;

	@Mock
	GroupService groupService;

	@Mock
	RulesService rulesService;

	@Mock
	GangRepository gangRepository;

	@Mock
	FighterRepository fighterRepository;

	@Mock
	FighterViewService fighterViewService;

	@Mock
	GroupEntity group;

	@Mock
	AccountEntity player;

	@Mock
	GangTypeEntity gangType;

	@Mock
	FactionEntity faction;

	@Mock
	GangEntity gang;

	@Mock
	FighterEntity fighter;

	@Mock
	FighterViewOutput fighterViewOutput;

	@Mock
	RulesOutput rules;

	@Mock
	GroupViewMembersOutput groupMembers;

	@Mock
	List<FactionReferenceOutput> factions;

	@Test
	public void prepareToCreateGang_ok() {
		final EditGangOutput result = instance.prepareToCreateGang(GROUP_ID);
		assertSame("Wrong group", groupMembers, result.getGroup());
		assertSame("Wrong rules", rules, result.getRules());
		assertSame("Wrong factions", factions, result.getFactions());
	}

	@Test
	public void prepareToEditGang_ok() {
		final EditGangOutput result = instance.prepareToEditGang(GROUP_ID, GANG_ID);
		assertSame("Wrong group", groupMembers, result.getGroup());
		assertSame("Wrong rules", rules, result.getRules());
		assertNull("Doesn't need to get factions", result.getFactions());
		assertEquals("Wrong gang", GANG_NAME, result.getCurrentState().getName());
	}

	@Test
	public void viewGang_ok() {
		final GangOutput result = instance.viewGang(GROUP_ID, GANG_ID);
		assertEquals("Wrong gang returned", GANG_NAME, result.getName());
	}

	@Test
	public void prepareToRecruitFighter_ok() {
		final GangRecruitOutput result = instance.prepareToRecruitFighter(GROUP_ID, GANG_ID);
		assertEquals("Wrong gang returned", GANG_NAME, result.getName());
		assertSame("Wrong rules returned", rules, result.getRules());
	}

	@Test
	public void viewFighter_ok() {
		final FighterViewOutput result = instance.viewFighter(GROUP_ID, FIGHTER_ID);
		assertEquals("Wrong fighter returned", FIGHTER_NAME, result.getName());
		assertEquals("Wrong gang returned", GANG_NAME, result.getGang().getName());
		assertSame("Wrong rules returned", rules, result.getGang().getRules());
	}

	@Before
	public void setup() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupService.viewGroupMembers(GROUP_ID)).thenReturn(groupMembers);
		when(rulesService.viewRules(GROUP_ID)).thenReturn(rules);
		when(rulesService.listAllFactions(GROUP_ID)).thenReturn(factions);
		when(gang.getId()).thenReturn(GANG_ID);
		when(gang.getName()).thenReturn(GANG_NAME);
		when(gang.getGroup()).thenReturn(group);
		when(gang.getPlayer()).thenReturn(player);
		when(gang.getFaction()).thenReturn(faction);
		when(gangRepository.load(GROUP_ID, GANG_ID)).thenReturn(gang);
		when(faction.getId()).thenReturn(FACTION_ID);
		when(faction.getGroup()).thenReturn(group);
		when(faction.getGangs()).thenReturn(new HashSet<GangEntity>());
		faction.getGangs().add(gang);
		when(faction.getGangType()).thenReturn(gangType);
		when(gangType.getId()).thenReturn(GANG_TYPE_ID);
		when(gangType.getGroup()).thenReturn(group);
		when(fighter.getId()).thenReturn(FIGHTER_ID);
		when(fighter.getName()).thenReturn(FIGHTER_NAME);
		when(fighter.getGang()).thenReturn(gang);
		when(fighterRepository.load(GROUP_ID, FIGHTER_ID)).thenReturn(fighter);
		when(fighterViewService.view(any(FighterEntity.class), any(GangOutput.class))).thenAnswer(new Answer<FighterViewOutput>(){
			@Override
			public FighterViewOutput answer(final InvocationOnMock invocation) throws Throwable {
				final FighterEntity fighter = (FighterEntity)invocation.getArguments()[0];
				final Long fighterId = fighter.getId();
				final String fighterName = fighter.getName();
				final GangOutput gang = (GangOutput)invocation.getArguments()[1];
				final FighterViewOutput output = mock(FighterViewOutput.class);
				when(output.getId()).thenReturn(fighterId);
				when(output.getName()).thenReturn(fighterName);
				when(output.getGang()).thenReturn(gang);
				return output;
			}
		});
	}

	@Before
	public void setupInstance() {
		instance = new GangService();
		instance.groupService = groupService;
		instance.rulesService = rulesService;
		instance.gangRepository = gangRepository;
		instance.fighterRepository = fighterRepository;
		instance.fighterViewService = fighterViewService;
	}
}

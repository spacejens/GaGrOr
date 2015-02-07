package com.gagror.service.wh40kskirmish.gangs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupViewMembersOutput;
import com.gagror.data.wh40kskirmish.gangs.EditGangOutput;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangOutput;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.service.social.GroupService;
import com.gagror.service.wh40kskirmish.rules.RulesService;

@RunWith(MockitoJUnitRunner.class)
public class GangServiceUnitTest {

	private static final Long GROUP_ID = 1L;
	private static final Long GANG_TYPE_ID = 2L;
	private static final Long FACTION_ID = 3L;
	private static final Long GANG_ID = 4L;
	private static final String GANG_NAME = "Gang";

	GangService instance;

	@Mock
	GroupService groupService;

	@Mock
	RulesService rulesService;

	@Mock
	FactionRepository factionRepository;

	@Mock
	AccountEntity player;

	@Mock
	GangTypeEntity gangType;

	@Mock
	FactionEntity faction;

	@Mock
	GangEntity gang;

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
		final EditGangOutput result = instance.prepareToEditGang(GROUP_ID, GANG_TYPE_ID, FACTION_ID, GANG_ID);
		assertSame("Wrong group", groupMembers, result.getGroup());
		assertSame("Wrong rules", rules, result.getRules());
		assertNull("Doesn't need to get factions", result.getFactions());
		assertEquals("Wrong gang", GANG_NAME, result.getCurrentState().getName());
	}

	@Test
	public void viewGang_ok() {
		final GangOutput result = instance.viewGang(GROUP_ID, GANG_TYPE_ID, FACTION_ID, GANG_ID);
		assertEquals("Wrong gang returned", GANG_NAME, result.getName());
	}

	@Test(expected = DataNotFoundException.class)
	public void viewGang_notFound() {
		faction.getGangs().remove(gang);
		instance.viewGang(GROUP_ID, GANG_TYPE_ID, FACTION_ID, GANG_ID);
	}

	@Before
	public void setup() {
		when(groupService.viewGroupMembers(GROUP_ID)).thenReturn(groupMembers);
		when(rulesService.viewRules(GROUP_ID)).thenReturn(rules);
		when(rulesService.listAllFactions(GROUP_ID)).thenReturn(factions);
		when(factionRepository.load(GROUP_ID, GANG_TYPE_ID, FACTION_ID)).thenReturn(faction);
		when(gang.getId()).thenReturn(GANG_ID);
		when(gang.getName()).thenReturn(GANG_NAME);
		when(gang.getPlayer()).thenReturn(player);
		when(gang.getFaction()).thenReturn(faction);
		when(faction.getId()).thenReturn(FACTION_ID);
		when(faction.getGangs()).thenReturn(new HashSet<GangEntity>());
		faction.getGangs().add(gang);
		when(faction.getGangType()).thenReturn(gangType);
		when(gangType.getId()).thenReturn(GANG_TYPE_ID);
	}

	@Before
	public void setupInstance() {
		instance = new GangService();
		instance.groupService = groupService;
		instance.rulesService = rulesService;
		instance.factionRepository = factionRepository;
	}
}

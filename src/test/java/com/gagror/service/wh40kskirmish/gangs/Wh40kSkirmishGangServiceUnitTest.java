package com.gagror.service.wh40kskirmish.gangs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.wh40kskirmish.gangs.Wh40kSkirmishGangEntity;
import com.gagror.data.wh40kskirmish.gangs.Wh40kSkirmishGangOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.service.wh40kskirmish.rules.Wh40kSkirmishRulesService;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishGangServiceUnitTest {

	private static final Long GROUP_ID = 1L;
	private static final Long GANG_TYPE_ID = 2L;
	private static final Long FACTION_ID = 3L;
	private static final Long GANG_ID = 4L;
	private static final String GANG_NAME = "Gang";

	Wh40kSkirmishGangService instance;

	@Mock
	Wh40kSkirmishRulesService rulesService;

	@Mock
	AccountEntity player;

	@Mock
	Wh40kSkirmishGangTypeEntity gangType;

	@Mock
	Wh40kSkirmishFactionEntity faction;

	@Mock
	Wh40kSkirmishGangEntity gang;

	@Test
	public void viewGang_ok() {
		final Wh40kSkirmishGangOutput result = instance.viewGang(GROUP_ID, GANG_TYPE_ID, FACTION_ID, GANG_ID);
		assertEquals("Wrong gang returned", GANG_NAME, result.getName());
	}

	@Test(expected = DataNotFoundException.class)
	public void viewGang_notFound() {
		faction.getGangs().remove(gang);
		instance.viewGang(GROUP_ID, GANG_TYPE_ID, FACTION_ID, GANG_ID);
	}

	@Before
	public void setup() {
		when(rulesService.loadFaction(GROUP_ID, GANG_TYPE_ID, FACTION_ID)).thenReturn(faction);
		when(gang.getId()).thenReturn(GANG_ID);
		when(gang.getName()).thenReturn(GANG_NAME);
		when(gang.getPlayer()).thenReturn(player);
		when(gang.getFaction()).thenReturn(faction);
		when(faction.getId()).thenReturn(FACTION_ID);
		when(faction.getGangs()).thenReturn(new HashSet<Wh40kSkirmishGangEntity>());
		faction.getGangs().add(gang);
		when(faction.getGangType()).thenReturn(gangType);
		when(gangType.getId()).thenReturn(GANG_TYPE_ID);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishGangService();
		instance.rulesService = rulesService;
	}
}

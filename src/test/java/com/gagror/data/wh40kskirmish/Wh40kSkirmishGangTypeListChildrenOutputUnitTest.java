package com.gagror.data.wh40kskirmish;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.group.GroupEntity;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishGangTypeListChildrenOutputUnitTest {

	private static final Long FACTION_ID_1 = 1L;
	private static final Long FACTION_ID_2 = 2L;
	private static final Long FACTION_ID_3 = 3L;
	private static final Long FACTION_ID_4 = 4L;
	private static final String FACTION_NAME_1 = "Name 1";
	private static final String FACTION_NAME_2 = "Name 2";
	private static final String FACTION_NAME_3 = "Name 3";
	private static final String FACTION_NAME_4 = "Name 4";
	private static final Long GROUP_ID = 64578L;

	@Mock
	Wh40kSkirmishFactionEntity faction1;

	@Mock
	Wh40kSkirmishFactionEntity faction2;

	@Mock
	Wh40kSkirmishFactionEntity faction3;

	@Mock
	Wh40kSkirmishFactionEntity faction4;

	@Mock
	Wh40kSkirmishGangTypeEntity gangType;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	GroupEntity group;

	@Test
	public void getFactions() {
		final Wh40kSkirmishGangTypeListChildrenOutput instance = new Wh40kSkirmishGangTypeListChildrenOutput(gangType);
		assertFactions(instance.getFactions(), FACTION_NAME_1, FACTION_NAME_2, FACTION_NAME_3, FACTION_NAME_4);
	}

	private void assertFactions(
			final List<Wh40kSkirmishFactionReferenceOutput> factions,
			final String... expectedFactions) {
		// TODO Extract this method (and others like it) as a shared test utility method (for any List of Named)
		final List<String> expected = Arrays.asList(expectedFactions);
		final List<String> actual = new ArrayList<>(factions.size());
		for(final Wh40kSkirmishFactionReferenceOutput faction : factions) {
			actual.add(faction.getName());
		}
		assertEquals("Wrong factions returned", expected, actual);
	}

	@Before
	public void setup() {
		when(gangType.getRules()).thenReturn(rules);
		when(rules.getGroup()).thenReturn(group);
		when(group.getId()).thenReturn(GROUP_ID);
		when(gangType.getFactions()).thenReturn(new HashSet<Wh40kSkirmishFactionEntity>());
		when(faction1.getId()).thenReturn(FACTION_ID_1);
		when(faction1.getName()).thenReturn(FACTION_NAME_1);
		gangType.getFactions().add(faction1);
		when(faction2.getId()).thenReturn(FACTION_ID_2);
		when(faction2.getName()).thenReturn(FACTION_NAME_2);
		gangType.getFactions().add(faction2);
		when(faction3.getId()).thenReturn(FACTION_ID_3);
		when(faction3.getName()).thenReturn(FACTION_NAME_3);
		gangType.getFactions().add(faction3);
		when(faction4.getId()).thenReturn(FACTION_ID_4);
		when(faction4.getName()).thenReturn(FACTION_NAME_4);
		gangType.getFactions().add(faction4);
	}
}

package com.gagror.service.wh40kskirmish;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishRulesEntity;
import com.gagror.service.social.GroupService;

@RunWith(MockitoJUnitRunner.class)
public class Wh40kSkirmishRulesServiceUnitTest {

	private static final Long GROUP_ID = 123L;
	private static final Long RULES_ID = 1L;
	private static final Long WRONG_TYPE_GROUP_ID = 456L;

	Wh40kSkirmishRulesService instance;

	@Mock
	GroupService groupService;

	@Mock
	GroupEntity groupEntity;

	@Mock
	Wh40kSkirmishRulesEntity rulesEntity;

	@Mock
	GroupEntity wrongTypeGroupEntity;

	@Test
	public void viewRules_ok() {
		assertEquals("Wrong rules returned", RULES_ID, instance.viewRules(GROUP_ID).getId());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewRules_wrongType() {
		instance.viewRules(WRONG_TYPE_GROUP_ID);
	}

	@Test
	public void viewRulesWithGangTypes_ok() {
		assertEquals("Wrong rules returned", RULES_ID, instance.viewRulesWithGangTypes(GROUP_ID).getId());
	}

	@Test(expected=IllegalArgumentException.class)
	public void viewRulesWithGangTypes_wrongType() {
		instance.viewRulesWithGangTypes(WRONG_TYPE_GROUP_ID);
	}

	@Before
	public void setupRules() {
		when(rulesEntity.getId()).thenReturn(RULES_ID);
	}

	@Before
	public void setupGroups() {
		mockGroup(groupEntity, GROUP_ID);
		when(groupEntity.getWh40kSkirmishRules()).thenReturn(rulesEntity);
		mockGroup(wrongTypeGroupEntity, WRONG_TYPE_GROUP_ID);
	}

	private void mockGroup(final GroupEntity group, final Long id) {
		when(group.getId()).thenReturn(id);
		when(groupService.loadGroup(id)).thenReturn(group);
	}

	@Before
	public void setupInstance() {
		instance = new Wh40kSkirmishRulesService();
		instance.groupService = groupService;
	}
}

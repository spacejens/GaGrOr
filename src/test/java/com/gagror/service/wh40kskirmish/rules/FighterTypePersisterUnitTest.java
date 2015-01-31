package com.gagror.service.wh40kskirmish.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import com.gagror.AddError;
import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceEntity;

@RunWith(MockitoJUnitRunner.class)
public class FighterTypePersisterUnitTest {

	private static final Long GROUP_ID = 2135L;
	private static final Long GANG_TYPE_ID = 5789L;
	private static final Long RACE_ID = 6724L;
	private static final String FORM_FIGHTERTYPE_NAME = "Fighter type form";
	private static final int FORM_FIGHTERTYPE_MOVEMENT = 1;
	private static final int FORM_FIGHTERTYPE_WEAPON_SKILL = 2;
	private static final int FORM_FIGHTERTYPE_BALLISTIC_SKILL = 3;
	private static final int FORM_FIGHTERTYPE_STRENGTH = 4;
	private static final int FORM_FIGHTERTYPE_TOUGHNESS = 5;
	private static final int FORM_FIGHTERTYPE_WOUNDS = 6;
	private static final int FORM_FIGHTERTYPE_INITIATIVE = 7;
	private static final int FORM_FIGHTERTYPE_ATTACKS = 8;
	private static final int FORM_FIGHTERTYPE_LEADERSHIP = 9;
	private static final Long DB_FIGHTERTYPE_ID = 11L;
	private static final String DB_FIGHTERTYPE_NAME = "Fighter type DB";
	private static final Long DB_FIGHTERTYPE_VERSION = 5L;

	FighterTypePersister instance;

	@Mock
	FighterTypeInput form;

	@Mock
	BindingResult bindingResult;

	@Mock
	GroupRepository groupRepository;

	@Mock
	FighterTypeRepository fighterTypeRepository;

	@Mock
	GroupEntity group;

	@Mock
	Wh40kSkirmishRulesEntity rules;

	@Mock
	GangTypeEntity gangType;

	@Mock
	RaceEntity race;

	@Mock
	FighterTypeEntity fighterType;

	@Test
	public void save_new_ok() {
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		final ArgumentCaptor<FighterTypeEntity> savedFighterType = ArgumentCaptor.forClass(FighterTypeEntity.class);
		verify(fighterTypeRepository).save(savedFighterType.capture());
		assertEquals("Wrong name", FORM_FIGHTERTYPE_NAME, savedFighterType.getValue().getName());
		assertEquals("Wrong movement", FORM_FIGHTERTYPE_MOVEMENT, savedFighterType.getValue().getStartingMovement());
		assertEquals("Wrong weapon skill", FORM_FIGHTERTYPE_WEAPON_SKILL, savedFighterType.getValue().getStartingWeaponSkill());
		assertEquals("Wrong ballistic skill", FORM_FIGHTERTYPE_BALLISTIC_SKILL, savedFighterType.getValue().getStartingBallisticSkill());
		assertEquals("Wrong strength", FORM_FIGHTERTYPE_STRENGTH, savedFighterType.getValue().getStartingStrength());
		assertEquals("Wrong toughness", FORM_FIGHTERTYPE_TOUGHNESS, savedFighterType.getValue().getStartingToughness());
		assertEquals("Wrong wounds", FORM_FIGHTERTYPE_WOUNDS, savedFighterType.getValue().getStartingWounds());
		assertEquals("Wrong initiative", FORM_FIGHTERTYPE_INITIATIVE, savedFighterType.getValue().getStartingInitiative());
		assertEquals("Wrong attacks", FORM_FIGHTERTYPE_ATTACKS, savedFighterType.getValue().getStartingAttacks());
		assertEquals("Wrong leadership", FORM_FIGHTERTYPE_LEADERSHIP, savedFighterType.getValue().getStartingLeadership());
		assertTrue("Not added to gang type", race.getFighterTypes().contains(savedFighterType.getValue()));
	}

	@Test
	public void save_new_nameNotUnique() {
		whenAnotherFighterTypeWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_new_maxMovement() {
		when(form.getStartingMovement()).thenReturn(FORM_FIGHTERTYPE_MOVEMENT + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(form).addErrorStartingAboveMaxMovement(bindingResult, FORM_FIGHTERTYPE_MOVEMENT);
	}

	@Test
	public void save_new_maxWeaponSkill() {
		when(form.getStartingWeaponSkill()).thenReturn(FORM_FIGHTERTYPE_WEAPON_SKILL + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(form).addErrorStartingAboveMaxWeaponSkill(bindingResult, FORM_FIGHTERTYPE_WEAPON_SKILL);
	}

	@Test
	public void save_new_maxBallisticSkill() {
		when(form.getStartingBallisticSkill()).thenReturn(FORM_FIGHTERTYPE_BALLISTIC_SKILL + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(form).addErrorStartingAboveMaxBallisticSkill(bindingResult, FORM_FIGHTERTYPE_BALLISTIC_SKILL);
	}

	@Test
	public void save_new_maxStrength() {
		when(form.getStartingStrength()).thenReturn(FORM_FIGHTERTYPE_STRENGTH + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(form).addErrorStartingAboveMaxStrength(bindingResult, FORM_FIGHTERTYPE_STRENGTH);
	}

	@Test
	public void save_new_maxToughness() {
		when(form.getStartingToughness()).thenReturn(FORM_FIGHTERTYPE_TOUGHNESS + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(form).addErrorStartingAboveMaxToughness(bindingResult, FORM_FIGHTERTYPE_TOUGHNESS);
	}

	@Test
	public void save_new_maxWounds() {
		when(form.getStartingWounds()).thenReturn(FORM_FIGHTERTYPE_WOUNDS + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(form).addErrorStartingAboveMaxWounds(bindingResult, FORM_FIGHTERTYPE_WOUNDS);
	}

	@Test
	public void save_new_maxInitiative() {
		when(form.getStartingInitiative()).thenReturn(FORM_FIGHTERTYPE_INITIATIVE + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(form).addErrorStartingAboveMaxInitiative(bindingResult, FORM_FIGHTERTYPE_INITIATIVE);
	}

	@Test
	public void save_new_maxAttacks() {
		when(form.getStartingAttacks()).thenReturn(FORM_FIGHTERTYPE_ATTACKS + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(form).addErrorStartingAboveMaxAttacks(bindingResult, FORM_FIGHTERTYPE_ATTACKS);
	}

	@Test
	public void save_new_maxLeadership() {
		when(form.getStartingLeadership()).thenReturn(FORM_FIGHTERTYPE_LEADERSHIP + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(form).addErrorStartingAboveMaxLeadership(bindingResult, FORM_FIGHTERTYPE_LEADERSHIP);
	}

	@Test
	public void save_new_bindingError() {
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_new_wrongGroupType() {
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_new_gangTypeNotFound() {
		rules.getGangTypes().remove(gangType);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_new_raceNotFound() {
		gangType.getRaces().remove(race);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_ok() {
		whenFighterTypeExists();
		final boolean result = instance.save(form, bindingResult);
		assertTrue("Should have saved successfully", result);
		assertFalse("Should not have reported errors", bindingResult.hasErrors());
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
		verify(fighterType).setName(FORM_FIGHTERTYPE_NAME);
		verify(fighterType).setStartingMovement(FORM_FIGHTERTYPE_MOVEMENT);
		verify(fighterType).setStartingWeaponSkill(FORM_FIGHTERTYPE_WEAPON_SKILL);
		verify(fighterType).setStartingBallisticSkill(FORM_FIGHTERTYPE_BALLISTIC_SKILL);
		verify(fighterType).setStartingStrength(FORM_FIGHTERTYPE_STRENGTH);
		verify(fighterType).setStartingToughness(FORM_FIGHTERTYPE_TOUGHNESS);
		verify(fighterType).setStartingWounds(FORM_FIGHTERTYPE_WOUNDS);
		verify(fighterType).setStartingInitiative(FORM_FIGHTERTYPE_INITIATIVE);
		verify(fighterType).setStartingAttacks(FORM_FIGHTERTYPE_ATTACKS);
		verify(fighterType).setStartingLeadership(FORM_FIGHTERTYPE_LEADERSHIP);
	}

	@Test
	public void save_existing_nameNotUnique() {
		whenFighterTypeExists();
		whenAnotherFighterTypeWithSameNameExists();
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setName(anyString());
		verify(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
	}

	@Test
	public void save_existing_maxMovement() {
		whenFighterTypeExists();
		when(form.getStartingMovement()).thenReturn(FORM_FIGHTERTYPE_MOVEMENT + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setStartingMovement(anyInt());
		verify(form).addErrorStartingAboveMaxMovement(bindingResult, FORM_FIGHTERTYPE_MOVEMENT);
	}

	@Test
	public void save_existing_maxWeaponSkill() {
		whenFighterTypeExists();
		when(form.getStartingWeaponSkill()).thenReturn(FORM_FIGHTERTYPE_WEAPON_SKILL + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setStartingWeaponSkill(anyInt());
		verify(form).addErrorStartingAboveMaxWeaponSkill(bindingResult, FORM_FIGHTERTYPE_WEAPON_SKILL);
	}

	@Test
	public void save_existing_maxBallisticSkill() {
		whenFighterTypeExists();
		when(form.getStartingBallisticSkill()).thenReturn(FORM_FIGHTERTYPE_BALLISTIC_SKILL + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setStartingBallisticSkill(anyInt());
		verify(form).addErrorStartingAboveMaxBallisticSkill(bindingResult, FORM_FIGHTERTYPE_BALLISTIC_SKILL);
	}

	@Test
	public void save_existing_maxStrength() {
		whenFighterTypeExists();
		when(form.getStartingStrength()).thenReturn(FORM_FIGHTERTYPE_STRENGTH + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setStartingStrength(anyInt());
		verify(form).addErrorStartingAboveMaxStrength(bindingResult, FORM_FIGHTERTYPE_STRENGTH);
	}

	@Test
	public void save_existing_maxToughness() {
		whenFighterTypeExists();
		when(form.getStartingToughness()).thenReturn(FORM_FIGHTERTYPE_TOUGHNESS + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setStartingToughness(anyInt());
		verify(form).addErrorStartingAboveMaxToughness(bindingResult, FORM_FIGHTERTYPE_TOUGHNESS);
	}

	@Test
	public void save_existing_maxWounds() {
		whenFighterTypeExists();
		when(form.getStartingWounds()).thenReturn(FORM_FIGHTERTYPE_WOUNDS + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setStartingWounds(anyInt());
		verify(form).addErrorStartingAboveMaxWounds(bindingResult, FORM_FIGHTERTYPE_WOUNDS);
	}

	@Test
	public void save_existing_maxInitiative() {
		whenFighterTypeExists();
		when(form.getStartingInitiative()).thenReturn(FORM_FIGHTERTYPE_INITIATIVE + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setStartingInitiative(anyInt());
		verify(form).addErrorStartingAboveMaxInitiative(bindingResult, FORM_FIGHTERTYPE_INITIATIVE);
	}

	@Test
	public void save_existing_maxAttacks() {
		whenFighterTypeExists();
		when(form.getStartingAttacks()).thenReturn(FORM_FIGHTERTYPE_ATTACKS + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setStartingAttacks(anyInt());
		verify(form).addErrorStartingAboveMaxAttacks(bindingResult, FORM_FIGHTERTYPE_ATTACKS);
	}

	@Test
	public void save_existing_maxLeadership() {
		whenFighterTypeExists();
		when(form.getStartingLeadership()).thenReturn(FORM_FIGHTERTYPE_LEADERSHIP + 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(fighterType, never()).setStartingLeadership(anyInt());
		verify(form).addErrorStartingAboveMaxLeadership(bindingResult, FORM_FIGHTERTYPE_LEADERSHIP);
	}

	@Test
	public void save_existing_bindingError() {
		whenFighterTypeExists();
		when(bindingResult.hasErrors()).thenReturn(true);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(race, never()).setName(anyString());
		verify(fighterTypeRepository, never()).save(any(FighterTypeEntity.class));
	}

	@Test(expected=WrongGroupTypeException.class)
	public void save_existing_wrongGroupType() {
		whenFighterTypeExists();
		when(group.getWh40kSkirmishRules()).thenReturn(null);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_gangTypeNotFound() {
		whenFighterTypeExists();
		rules.getGangTypes().remove(gangType);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_raceNotFound() {
		whenFighterTypeExists();
		gangType.getRaces().remove(race);
		instance.save(form, bindingResult);
	}

	@Test(expected=DataNotFoundException.class)
	public void save_existing_notFoundInRace() {
		whenFighterTypeExists();
		race.getFighterTypes().remove(fighterType);
		instance.save(form, bindingResult);
	}

	@Test
	public void save_existing_simultaneousEdit() {
		whenFighterTypeExists();
		when(form.getVersion()).thenReturn(DB_FIGHTERTYPE_VERSION - 1);
		final boolean result = instance.save(form, bindingResult);
		assertFalse("Should have failed to save", result);
		verify(form).addErrorSimultaneuosEdit(bindingResult);
		verify(race, never()).setName(anyString());
	}

	protected void whenAnotherFighterTypeWithSameNameExists() {
		final GangTypeEntity anotherGangType = mock(GangTypeEntity.class);
		when(anotherGangType.getId()).thenReturn(GANG_TYPE_ID + 1);
		when(anotherGangType.getRaces()).thenReturn(new HashSet<RaceEntity>());
		rules.getGangTypes().add(anotherGangType);
		final RaceEntity anotherRace = mock(RaceEntity.class);
		when(anotherRace.getId()).thenReturn(RACE_ID + 1);
		when(anotherRace.getFighterTypes()).thenReturn(new HashSet<FighterTypeEntity>());
		anotherGangType.getRaces().add(anotherRace);
		final FighterTypeEntity anotherFighterType = mock(FighterTypeEntity.class);
		when(anotherFighterType.getId()).thenReturn(DB_FIGHTERTYPE_ID + 1);
		when(anotherFighterType.getName()).thenReturn(FORM_FIGHTERTYPE_NAME);
		anotherRace.getFighterTypes().add(anotherFighterType);
	}

	protected void whenFighterTypeExists() {
		when(form.getId()).thenReturn(DB_FIGHTERTYPE_ID);
		when(form.getVersion()).thenReturn(DB_FIGHTERTYPE_VERSION);
		when(fighterType.getId()).thenReturn(DB_FIGHTERTYPE_ID);
		when(fighterType.getVersion()).thenReturn(DB_FIGHTERTYPE_VERSION);
		when(fighterType.getName()).thenReturn(DB_FIGHTERTYPE_NAME);
		when(fighterType.getRace()).thenReturn(race);
		race.getFighterTypes().add(fighterType);
	}

	@Before
	public void setupForm() {
		when(form.getGroupId()).thenReturn(GROUP_ID);
		when(form.getGangTypeId()).thenReturn(GANG_TYPE_ID);
		when(form.getRaceId()).thenReturn(RACE_ID);
		when(form.getName()).thenReturn(FORM_FIGHTERTYPE_NAME);
		when(form.getStartingMovement()).thenReturn(FORM_FIGHTERTYPE_MOVEMENT);
		when(form.getStartingWeaponSkill()).thenReturn(FORM_FIGHTERTYPE_WEAPON_SKILL);
		when(form.getStartingBallisticSkill()).thenReturn(FORM_FIGHTERTYPE_BALLISTIC_SKILL);
		when(form.getStartingStrength()).thenReturn(FORM_FIGHTERTYPE_STRENGTH);
		when(form.getStartingToughness()).thenReturn(FORM_FIGHTERTYPE_TOUGHNESS);
		when(form.getStartingWounds()).thenReturn(FORM_FIGHTERTYPE_WOUNDS);
		when(form.getStartingInitiative()).thenReturn(FORM_FIGHTERTYPE_INITIATIVE);
		when(form.getStartingAttacks()).thenReturn(FORM_FIGHTERTYPE_ATTACKS);
		when(form.getStartingLeadership()).thenReturn(FORM_FIGHTERTYPE_LEADERSHIP);
		AddError.to(bindingResult).when(form).addErrorNameMustBeUniqueWithinGroup(bindingResult);
		AddError.to(bindingResult).when(form).addErrorSimultaneuosEdit(bindingResult);
		AddError.to(bindingResult).when(form).addErrorStartingAboveMaxMovement(bindingResult, FORM_FIGHTERTYPE_MOVEMENT);
		AddError.to(bindingResult).when(form).addErrorStartingAboveMaxWeaponSkill(bindingResult, FORM_FIGHTERTYPE_WEAPON_SKILL);
		AddError.to(bindingResult).when(form).addErrorStartingAboveMaxBallisticSkill(bindingResult, FORM_FIGHTERTYPE_BALLISTIC_SKILL);
		AddError.to(bindingResult).when(form).addErrorStartingAboveMaxStrength(bindingResult, FORM_FIGHTERTYPE_STRENGTH);
		AddError.to(bindingResult).when(form).addErrorStartingAboveMaxToughness(bindingResult, FORM_FIGHTERTYPE_TOUGHNESS);
		AddError.to(bindingResult).when(form).addErrorStartingAboveMaxWounds(bindingResult, FORM_FIGHTERTYPE_WOUNDS);
		AddError.to(bindingResult).when(form).addErrorStartingAboveMaxInitiative(bindingResult, FORM_FIGHTERTYPE_INITIATIVE);
		AddError.to(bindingResult).when(form).addErrorStartingAboveMaxAttacks(bindingResult, FORM_FIGHTERTYPE_ATTACKS);
		AddError.to(bindingResult).when(form).addErrorStartingAboveMaxLeadership(bindingResult, FORM_FIGHTERTYPE_LEADERSHIP);
	}

	@Before
	public void setupContext() {
		when(group.getId()).thenReturn(GROUP_ID);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(group);
		when(group.getWh40kSkirmishRules()).thenReturn(rules);
		when(rules.getGangTypes()).thenReturn(new HashSet<GangTypeEntity>());
		when(gangType.getId()).thenReturn(GANG_TYPE_ID);
		when(gangType.getRaces()).thenReturn(new HashSet<RaceEntity>());
		when(gangType.getRules()).thenReturn(rules);
		rules.getGangTypes().add(gangType);
		when(race.getId()).thenReturn(RACE_ID);
		when(race.getFighterTypes()).thenReturn(new HashSet<FighterTypeEntity>());
		when(race.getGangType()).thenReturn(gangType);
		when(race.getMaxMovement()).thenReturn(FORM_FIGHTERTYPE_MOVEMENT);
		when(race.getMaxWeaponSkill()).thenReturn(FORM_FIGHTERTYPE_WEAPON_SKILL);
		when(race.getMaxBallisticSkill()).thenReturn(FORM_FIGHTERTYPE_BALLISTIC_SKILL);
		when(race.getMaxStrength()).thenReturn(FORM_FIGHTERTYPE_STRENGTH);
		when(race.getMaxToughness()).thenReturn(FORM_FIGHTERTYPE_TOUGHNESS);
		when(race.getMaxWounds()).thenReturn(FORM_FIGHTERTYPE_WOUNDS);
		when(race.getMaxInitiative()).thenReturn(FORM_FIGHTERTYPE_INITIATIVE);
		when(race.getMaxAttacks()).thenReturn(FORM_FIGHTERTYPE_ATTACKS);
		when(race.getMaxLeadership()).thenReturn(FORM_FIGHTERTYPE_LEADERSHIP);
		gangType.getRaces().add(race);
	}

	@Before
	public void setupInstance() {
		instance = new FighterTypePersister();
		instance.groupRepository = groupRepository;
		instance.fighterTypeRepository = fighterTypeRepository;
	}
}

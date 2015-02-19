package com.gagror.service.wh40kskirmish.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceInput;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceRepository;
import com.gagror.service.AbstractIdentifiablePersister;

@Service
public class RacePersister
extends AbstractIdentifiablePersister<RaceInput, RaceEntity, GangTypeEntity> {

	@Autowired
	GangTypeRepository gangTypeRepository;

	@Autowired
	RaceRepository raceRepository;

	@Override
	protected void validateForm(final RaceInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected GangTypeEntity loadContext(final RaceInput form) {
		return gangTypeRepository.load(form.getGroupId(), form.getGangTypeId());
	}

	@Override
	protected void validateFormVsContext(
			final RaceInput form,
			final BindingResult bindingResult,
			final GangTypeEntity context) {
		for(final GangTypeEntity gangType : context.getRules().getGangTypes()) {
			for(final RaceEntity race : gangType.getRaces()) {
				if(race.getName().equals(form.getName())
						&& ! race.hasId(form.getId())) {
					form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
				}
			}
		}
	}

	@Override
	protected RaceEntity loadExisting(final RaceInput form, final GangTypeEntity context) {
		for(final RaceEntity race : context.getRaces()) {
			if(race.hasId(form.getId())) {
				return race;
			}
		}
		throw new DataNotFoundException(String.format("Race %d (gang type %d, group %d)", form.getId(), form.getGangTypeId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final RaceInput form,
			final BindingResult bindingResult,
			final RaceEntity entity) {
		// Validate that maximum characteristics are not below fighter type maximums
		int minMovement = 0;
		int minWeaponSkill = 0;
		int minBallisticSkill = 0;
		int minStrength = 0;
		int minToughness = 0;
		int minWounds = 0;
		int minInitiative = 0;
		int minAttacks = 0;
		int minLeadership = 0;
		for(final FighterTypeEntity fighterType : entity.getFighterTypes()) {
			minMovement = Math.max(minMovement, fighterType.getStartingMovement());
			minWeaponSkill = Math.max(minWeaponSkill, fighterType.getStartingWeaponSkill());
			minBallisticSkill = Math.max(minBallisticSkill, fighterType.getStartingBallisticSkill());
			minStrength = Math.max(minStrength, fighterType.getStartingStrength());
			minToughness = Math.max(minToughness, fighterType.getStartingToughness());
			minWounds = Math.max(minWounds, fighterType.getStartingWounds());
			minInitiative = Math.max(minInitiative, fighterType.getStartingInitiative());
			minAttacks = Math.max(minAttacks, fighterType.getStartingAttacks());
			minLeadership = Math.max(minLeadership, fighterType.getStartingLeadership());
		}
		if(form.getMaxMovement() < minMovement) {
			form.addErrorMaxBelowStartingMovement(bindingResult, minMovement);
		}
		if(form.getMaxWeaponSkill() < minWeaponSkill) {
			form.addErrorMaxBelowStartingWeaponSkill(bindingResult, minWeaponSkill);
		}
		if(form.getMaxBallisticSkill() < minBallisticSkill) {
			form.addErrorMaxBelowStartingBallisticSkill(bindingResult, minBallisticSkill);
		}
		if(form.getMaxStrength() < minStrength) {
			form.addErrorMaxBelowStartingStrength(bindingResult, minStrength);
		}
		if(form.getMaxToughness() < minToughness) {
			form.addErrorMaxBelowStartingToughness(bindingResult, minToughness);
		}
		if(form.getMaxWounds() < minWounds) {
			form.addErrorMaxBelowStartingWounds(bindingResult, minWounds);
		}
		if(form.getMaxInitiative() < minInitiative) {
			form.addErrorMaxBelowStartingInitiative(bindingResult, minInitiative);
		}
		if(form.getMaxAttacks() < minAttacks) {
			form.addErrorMaxBelowStartingAttacks(bindingResult, minAttacks);
		}
		if(form.getMaxLeadership() < minLeadership) {
			form.addErrorMaxBelowStartingLeadership(bindingResult, minLeadership);
		}
	}

	@Override
	protected RaceEntity createNew(final RaceInput form, final GangTypeEntity context) {
		return new RaceEntity(context);
	}

	@Override
	protected void updateValues(final RaceInput form, final RaceEntity entity) {
		entity.setName(form.getName());
		entity.setMaxMovement(form.getMaxMovement());
		entity.setMaxWeaponSkill(form.getMaxWeaponSkill());
		entity.setMaxBallisticSkill(form.getMaxBallisticSkill());
		entity.setMaxStrength(form.getMaxStrength());
		entity.setMaxToughness(form.getMaxToughness());
		entity.setMaxWounds(form.getMaxWounds());
		entity.setMaxInitiative(form.getMaxInitiative());
		entity.setMaxAttacks(form.getMaxAttacks());
		entity.setMaxLeadership(form.getMaxLeadership());
	}

	@Override
	protected RaceEntity makePersistent(final RaceEntity entity) {
		return raceRepository.persist(entity);
	}
}

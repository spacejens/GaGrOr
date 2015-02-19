package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceRepository;
import com.gagror.service.AbstractIdentifiablePersister;

@Service
@CommonsLog
public class FighterTypePersister
extends AbstractIdentifiablePersister<FighterTypeInput, FighterTypeEntity, RaceEntity> {

	@Autowired
	RaceRepository raceRepository;

	@Autowired
	FighterTypeRepository fighterTypeRepository;

	@Override
	protected void validateForm(final FighterTypeInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected RaceEntity loadContext(final FighterTypeInput form) {
		return raceRepository.load(form.getGroupId(), form.getRaceId());
	}

	@Override
	protected void validateFormVsContext(
			final FighterTypeInput form,
			final BindingResult bindingResult,
			final RaceEntity context) {
		for(final GangTypeEntity gangType : context.getGangType().getRules().getGangTypes()) {
			for(final RaceEntity race : gangType.getRaces()) {
				for(final FighterTypeEntity fighterType : race.getFighterTypes()) {
					if(fighterType.getName().equals(form.getName())
							&& ! fighterType.hasId(form.getId())) {
						form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
					}
				}
			}
		}
		// Validate that starting characteristics are not above racial maximums
		if(form.getStartingMovement() > context.getMaxMovement()) {
			form.addErrorStartingAboveMaxMovement(bindingResult, context.getMaxMovement());
		}
		if(form.getStartingWeaponSkill() > context.getMaxWeaponSkill()) {
			form.addErrorStartingAboveMaxWeaponSkill(bindingResult, context.getMaxWeaponSkill());
		}
		if(form.getStartingBallisticSkill() > context.getMaxBallisticSkill()) {
			form.addErrorStartingAboveMaxBallisticSkill(bindingResult, context.getMaxBallisticSkill());
		}
		if(form.getStartingStrength() > context.getMaxStrength()) {
			form.addErrorStartingAboveMaxStrength(bindingResult, context.getMaxStrength());
		}
		if(form.getStartingToughness() > context.getMaxToughness()) {
			form.addErrorStartingAboveMaxToughness(bindingResult, context.getMaxToughness());
		}
		if(form.getStartingWounds() > context.getMaxWounds()) {
			form.addErrorStartingAboveMaxWounds(bindingResult, context.getMaxWounds());
		}
		if(form.getStartingInitiative() > context.getMaxInitiative()) {
			form.addErrorStartingAboveMaxInitiative(bindingResult, context.getMaxInitiative());
		}
		if(form.getStartingAttacks() > context.getMaxAttacks()) {
			form.addErrorStartingAboveMaxAttacks(bindingResult, context.getMaxAttacks());
		}
		if(form.getStartingLeadership() > context.getMaxLeadership()) {
			form.addErrorStartingAboveMaxLeadership(bindingResult, context.getMaxLeadership());
		}
	}

	@Override
	protected FighterTypeEntity loadExisting(final FighterTypeInput form, final RaceEntity context) {
		for(final FighterTypeEntity fighterType : context.getFighterTypes()) {
			if(fighterType.hasId(form.getId())) {
				return fighterType;
			}
		}
		throw new DataNotFoundException(String.format("Fighter type (race %d, group %d)", form.getId(), form.getRaceId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final FighterTypeInput form,
			final BindingResult bindingResult,
			final FighterTypeEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit fighter type %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected FighterTypeEntity createNew(final FighterTypeInput form, final RaceEntity context) {
		return new FighterTypeEntity(context);
	}

	@Override
	protected void updateValues(final FighterTypeInput form, final FighterTypeEntity entity) {
		entity.setName(form.getName());
		entity.setStartingMovement(form.getStartingMovement());
		entity.setStartingWeaponSkill(form.getStartingWeaponSkill());
		entity.setStartingBallisticSkill(form.getStartingBallisticSkill());
		entity.setStartingStrength(form.getStartingStrength());
		entity.setStartingToughness(form.getStartingToughness());
		entity.setStartingWounds(form.getStartingWounds());
		entity.setStartingInitiative(form.getStartingInitiative());
		entity.setStartingAttacks(form.getStartingAttacks());
		entity.setStartingLeadership(form.getStartingLeadership());
		entity.setCost(form.getCost());
	}

	@Override
	protected FighterTypeEntity makePersistent(final FighterTypeEntity entity) {
		return fighterTypeRepository.persist(entity);
	}
}

package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceInput;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class RacePersister
extends AbstractPersister<RaceInput, RaceEntity, GangTypeEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	RaceRepository raceRepository;

	@Override
	protected void validateForm(final RaceInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected GangTypeEntity loadContext(final RaceInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		for(final GangTypeEntity gangType : group.getWh40kSkirmishRules().getGangTypes()) {
			if(gangType.getId().equals(form.getGangTypeId())) {
				return gangType;
			}
		}
		throw new DataNotFoundException(String.format("Gang type %d (group %d)", form.getGangTypeId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsContext(
			final RaceInput form,
			final BindingResult bindingResult,
			final GangTypeEntity context) {
		for(final GangTypeEntity gangType : context.getRules().getGangTypes()) {
			for(final RaceEntity race : gangType.getRaces()) {
				if(race.getName().equals(form.getName())
						&& ! race.getId().equals(form.getId())) {
					form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
				}
			}
		}
	}

	@Override
	protected boolean isCreateNew(final RaceInput form) {
		return null == form.getId();
	}

	@Override
	protected RaceEntity loadExisting(final RaceInput form, final GangTypeEntity context) {
		for(final RaceEntity race : context.getRaces()) {
			if(race.getId().equals(form.getId())) {
				return race;
			}
		}
		throw new DataNotFoundException(String.format("Race %d (gang type %d, group %d)", form.getId(), form.getGangTypeId(), form.getGroupId()));
	}

	// TODO Validate that maximum stats are not set below highest fighter type starting stats

	@Override
	protected void validateFormVsExistingState(
			final RaceInput form,
			final BindingResult bindingResult,
			final RaceEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit race %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
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
		return raceRepository.save(entity);
	}
}

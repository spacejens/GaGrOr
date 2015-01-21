package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishRacePersister
extends AbstractPersister<Wh40kSkirmishRaceInput, Wh40kSkirmishRaceEntity, Wh40kSkirmishGangTypeEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishRaceRepository raceRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishRaceInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishGangTypeEntity loadContext(final Wh40kSkirmishRaceInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		for(final Wh40kSkirmishGangTypeEntity gangType : group.getWh40kSkirmishRules().getGangTypes()) {
			if(gangType.getId().equals(form.getGangTypeId())) {
				return gangType;
			}
		}
		throw new DataNotFoundException(String.format("Gang type %d (group %d)", form.getGangTypeId(), form.getGroupId()));
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishRaceInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishRaceEntity loadExisting(final Wh40kSkirmishRaceInput form, final Wh40kSkirmishGangTypeEntity context) {
		for(final Wh40kSkirmishRaceEntity race : context.getRaces()) {
			if(race.getId().equals(form.getId())) {
				return race;
			}
		}
		throw new DataNotFoundException(String.format("Race %d (gang type %d, group %d)", form.getId(), form.getGangTypeId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishRaceInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishRaceEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit race %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishRaceEntity createNew(final Wh40kSkirmishRaceInput form, final Wh40kSkirmishGangTypeEntity context) {
		return new Wh40kSkirmishRaceEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishRaceInput form, final Wh40kSkirmishRaceEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected Wh40kSkirmishRaceEntity makePersistent(final Wh40kSkirmishRaceEntity entity) {
		return raceRepository.save(entity);
	}
}

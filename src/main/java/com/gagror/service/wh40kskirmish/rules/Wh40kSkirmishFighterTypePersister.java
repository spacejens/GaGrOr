package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceEntity;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishFighterTypePersister
extends AbstractPersister<Wh40kSkirmishFighterTypeInput, Wh40kSkirmishFighterTypeEntity, Wh40kSkirmishRaceEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishFighterTypeRepository fighterTypeRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishFighterTypeInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishRaceEntity loadContext(final Wh40kSkirmishFighterTypeInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		for(final Wh40kSkirmishGangTypeEntity gangType : group.getWh40kSkirmishRules().getGangTypes()) {
			if(gangType.getId().equals(form.getGangTypeId())) {
				for(final Wh40kSkirmishRaceEntity race : gangType.getRaces()) {
					if(race.getId().equals(form.getRaceId())) {
						return race;
					}
				}
			}
		}
		throw new DataNotFoundException(String.format("Race %d (gang type %d, group %d)", form.getRaceId(), form.getGangTypeId(), form.getGroupId()));
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishFighterTypeInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishFighterTypeEntity loadExisting(final Wh40kSkirmishFighterTypeInput form, final Wh40kSkirmishRaceEntity context) {
		for(final Wh40kSkirmishFighterTypeEntity fighterType : context.getFighterTypes()) {
			if(fighterType.getId().equals(form.getId())) {
				return fighterType;
			}
		}
		throw new DataNotFoundException(String.format("Fighter type (race %d, gang type %d, group %d)", form.getId(), form.getRaceId(), form.getGangTypeId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishFighterTypeInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishFighterTypeEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit fighter type %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishFighterTypeEntity createNew(final Wh40kSkirmishFighterTypeInput form, final Wh40kSkirmishRaceEntity context) {
		return new Wh40kSkirmishFighterTypeEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishFighterTypeInput form, final Wh40kSkirmishFighterTypeEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected Wh40kSkirmishFighterTypeEntity makePersistent(final Wh40kSkirmishFighterTypeEntity entity) {
		return fighterTypeRepository.save(entity);
	}
}

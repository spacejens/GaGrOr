package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishGangTypePersister
extends AbstractPersister<Wh40kSkirmishGangTypeInput, Wh40kSkirmishGangTypeEntity, Wh40kSkirmishRulesEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishGangTypeRepository gangTypeRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishGangTypeInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadContext(final Wh40kSkirmishGangTypeInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishGangTypeInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishGangTypeEntity loadExisting(final Wh40kSkirmishGangTypeInput form, final Wh40kSkirmishRulesEntity context) {
		for(final Wh40kSkirmishGangTypeEntity gangType : context.getGangTypes()) {
			if(gangType.getId().equals(form.getId())) {
				return gangType;
			}
		}
		throw new IllegalStateException(String.format("Failed to find gang type %d when editing", form.getId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishGangTypeInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishGangTypeEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit gang type %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishGangTypeEntity createNew(final Wh40kSkirmishGangTypeInput form, final Wh40kSkirmishRulesEntity context) {
		return new Wh40kSkirmishGangTypeEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishGangTypeInput form, final Wh40kSkirmishGangTypeEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected Wh40kSkirmishGangTypeEntity makePersistent(final Wh40kSkirmishGangTypeEntity entity) {
		return gangTypeRepository.save(entity);
	}
}

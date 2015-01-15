package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishFactionEntity;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishFactionInput;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishFactionRepository;
import com.gagror.data.wh40kskirmish.Wh40kSkirmishGangTypeEntity;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishFactionPersister
extends AbstractPersister<Wh40kSkirmishFactionInput, Wh40kSkirmishFactionEntity, Wh40kSkirmishGangTypeEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishFactionRepository factionRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishFactionInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishGangTypeEntity loadContext(final Wh40kSkirmishFactionInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new IllegalStateException(String.format("Group %s lacks WH40K skirmish rules", group));
		}
		for(final Wh40kSkirmishGangTypeEntity gangType : group.getWh40kSkirmishRules().getGangTypes()) {
			if(gangType.getId().equals(form.getGangTypeId())) {
				return gangType;
			}
		}
		throw new IllegalStateException(String.format("Group %s does not have gang type %d", group, form.getGangTypeId()));
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishFactionInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishFactionEntity loadExisting(final Wh40kSkirmishFactionInput form, final Wh40kSkirmishGangTypeEntity context) {
		for(final Wh40kSkirmishFactionEntity faction : context.getFactions()) {
			if(faction.getId().equals(form.getId())) {
				return faction;
			}
		}
		throw new IllegalStateException(String.format("Failed to find faction %d when editing", form.getId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishFactionInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishFactionEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit faction %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishFactionEntity createNew(final Wh40kSkirmishFactionInput form, final Wh40kSkirmishGangTypeEntity context) {
		return new Wh40kSkirmishFactionEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishFactionInput form, final Wh40kSkirmishFactionEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected Wh40kSkirmishFactionEntity makePersistent(final Wh40kSkirmishFactionEntity entity) {
		return factionRepository.save(entity);
	}
}

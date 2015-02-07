package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionInput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class FactionPersister
extends AbstractPersister<FactionInput, FactionEntity, GangTypeEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	FactionRepository factionRepository;

	@Override
	protected void validateForm(final FactionInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected GangTypeEntity loadContext(final FactionInput form) {
		final GroupEntity group = groupRepository.load(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		for(final GangTypeEntity gangType : group.getWh40kSkirmishRules().getGangTypes()) {
			if(gangType.hasId(form.getGangTypeId())) {
				return gangType;
			}
		}
		throw new DataNotFoundException(String.format("Gang type %d (group %d)", form.getGangTypeId(), group.getId()));
	}

	@Override
	protected void validateFormVsContext(
			final FactionInput form,
			final BindingResult bindingResult,
			final GangTypeEntity context) {
		for(final GangTypeEntity gangType : context.getRules().getGangTypes()) {
			for(final FactionEntity faction : gangType.getFactions()) {
				if(faction.getName().equals(form.getName())
						&& ! faction.hasId(form.getId())) {
					form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
				}
			}
		}
	}

	@Override
	protected boolean isCreateNew(final FactionInput form) {
		return !form.isPersistent();
	}

	@Override
	protected FactionEntity loadExisting(final FactionInput form, final GangTypeEntity context) {
		for(final FactionEntity faction : context.getFactions()) {
			if(faction.hasId(form.getId())) {
				return faction;
			}
		}
		throw new DataNotFoundException(String.format("Faction %d (gang type %d, group %d)", form.getId(), context.getId(), context.getRules().getGroup().getId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final FactionInput form,
			final BindingResult bindingResult,
			final FactionEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit faction %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected FactionEntity createNew(final FactionInput form, final GangTypeEntity context) {
		return new FactionEntity(context);
	}

	@Override
	protected void updateValues(final FactionInput form, final FactionEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected FactionEntity makePersistent(final FactionEntity entity) {
		return factionRepository.persist(entity);
	}
}

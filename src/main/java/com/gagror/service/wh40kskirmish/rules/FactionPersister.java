package com.gagror.service.wh40kskirmish.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionInput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeRepository;
import com.gagror.service.AbstractIdentifiablePersister;

@Service
public class FactionPersister
extends AbstractIdentifiablePersister<FactionInput, FactionEntity, GangTypeEntity> {

	@Autowired
	GangTypeRepository gangTypeRepository;

	@Autowired
	FactionRepository factionRepository;

	@Override
	protected void validateForm(final FactionInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected GangTypeEntity loadContext(final FactionInput form) {
		return gangTypeRepository.load(form.getGroupId(), form.getGangTypeId());
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
	protected FactionEntity loadExisting(final FactionInput form, final GangTypeEntity context) {
		for(final FactionEntity faction : context.getFactions()) {
			if(faction.hasId(form.getId())) {
				return faction;
			}
		}
		throw new DataNotFoundException(String.format("Faction %d (gang type %d, group %d)", form.getId(), context.getId(), context.getRules().getGroup().getId()));
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

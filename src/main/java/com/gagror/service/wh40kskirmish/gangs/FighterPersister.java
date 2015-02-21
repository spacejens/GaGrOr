package com.gagror.service.wh40kskirmish.gangs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.wh40kskirmish.gangs.FighterEntity;
import com.gagror.data.wh40kskirmish.gangs.FighterInput;
import com.gagror.data.wh40kskirmish.gangs.FighterRepository;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.service.AbstractIdentifiablePersister;

@Service
public class FighterPersister
extends AbstractIdentifiablePersister<FighterInput, FighterEntity, GangEntity> {

	@Autowired
	GangRepository gangRepository;

	@Autowired
	FighterRepository fighterRepository;

	@Override
	protected void validateForm(final FighterInput form, final BindingResult bindingResult) {
		// Nothing to validate that isn't already validated by annotations
	}

	@Override
	protected GangEntity loadContext(final FighterInput form) {
		return gangRepository.load(form.getGroupId(), form.getGangId());
	}

	@Override
	protected void validateFormVsContext(final FighterInput form, final BindingResult bindingResult, final GangEntity context) {
		// Validate that the fighter name is not busy
		for(final GangTypeEntity gangType : context.getRules().getGangTypes()) {
			for(final FactionEntity faction : gangType.getFactions()) {
				for(final GangEntity gang : faction.getGangs()) {
					for(final FighterEntity fighter : gang.getFighters()) {
						if(! fighter.getId().equals(form.getId())
								&& form.getName().equals(fighter.getName())) {
							form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
						}
					}
				}
			}
		}
	}

	@Override
	protected boolean isCreateNew(final FighterInput form) {
		// Recruiting new fighters uses a separate persister
		return false;
	}

	@Override
	protected FighterEntity loadExisting(final FighterInput form, final GangEntity context) {
		return fighterRepository.load(form.getGroupId(), form.getId());
	}

	@Override
	protected void updateValues(final FighterInput form, final FighterEntity entity) {
		entity.setName(form.getName());
	}
}

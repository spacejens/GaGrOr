package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryInput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishTerritoryCategoryPersister
extends AbstractPersister<Wh40kSkirmishTerritoryCategoryInput, Wh40kSkirmishTerritoryCategoryEntity, Wh40kSkirmishRulesEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishTerritoryCategoryRepository territoryCategoryRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishTerritoryCategoryInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadContext(final Wh40kSkirmishTerritoryCategoryInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}

	@Override
	protected void validateFormVsContext(
			final Wh40kSkirmishTerritoryCategoryInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishRulesEntity context) {
		for(final Wh40kSkirmishTerritoryCategoryEntity territoryCategory : context.getTerritoryCategories()) {
			if(territoryCategory.getName().equals(form.getName())
					&& ! territoryCategory.getId().equals(form.getId())) {
				form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
			}
		}
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishTerritoryCategoryInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishTerritoryCategoryEntity loadExisting(final Wh40kSkirmishTerritoryCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		for(final Wh40kSkirmishTerritoryCategoryEntity territoryCategory : context.getTerritoryCategories()) {
			if(territoryCategory.getId().equals(form.getId())) {
				return territoryCategory;
			}
		}
		throw new DataNotFoundException(String.format("Territory category %d (group %d)", form.getId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishTerritoryCategoryInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishTerritoryCategoryEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit territory category %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishTerritoryCategoryEntity createNew(final Wh40kSkirmishTerritoryCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		return new Wh40kSkirmishTerritoryCategoryEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishTerritoryCategoryInput form, final Wh40kSkirmishTerritoryCategoryEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected Wh40kSkirmishTerritoryCategoryEntity makePersistent(final Wh40kSkirmishTerritoryCategoryEntity entity) {
		return territoryCategoryRepository.save(entity);
	}
}

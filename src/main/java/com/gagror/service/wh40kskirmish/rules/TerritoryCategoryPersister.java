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
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryInput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class TerritoryCategoryPersister
extends AbstractPersister<TerritoryCategoryInput, TerritoryCategoryEntity, Wh40kSkirmishRulesEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	TerritoryCategoryRepository territoryCategoryRepository;

	@Override
	protected void validateForm(final TerritoryCategoryInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadContext(final TerritoryCategoryInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}

	@Override
	protected void validateFormVsContext(
			final TerritoryCategoryInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishRulesEntity context) {
		for(final TerritoryCategoryEntity territoryCategory : context.getTerritoryCategories()) {
			if(territoryCategory.getName().equals(form.getName())
					&& ! territoryCategory.hasId(form.getId())) {
				form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
			}
		}
	}

	@Override
	protected boolean isCreateNew(final TerritoryCategoryInput form) {
		return !form.isPersistent();
	}

	@Override
	protected TerritoryCategoryEntity loadExisting(final TerritoryCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		for(final TerritoryCategoryEntity territoryCategory : context.getTerritoryCategories()) {
			if(territoryCategory.hasId(form.getId())) {
				return territoryCategory;
			}
		}
		throw new DataNotFoundException(String.format("Territory category %d (group %d)", form.getId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final TerritoryCategoryInput form,
			final BindingResult bindingResult,
			final TerritoryCategoryEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit territory category %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected TerritoryCategoryEntity createNew(final TerritoryCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		return new TerritoryCategoryEntity(context);
	}

	@Override
	protected void updateValues(final TerritoryCategoryInput form, final TerritoryCategoryEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected TerritoryCategoryEntity makePersistent(final TerritoryCategoryEntity entity) {
		return territoryCategoryRepository.save(entity);
	}
}

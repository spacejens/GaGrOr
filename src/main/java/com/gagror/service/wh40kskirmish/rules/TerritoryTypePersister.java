package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeInput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class TerritoryTypePersister
extends AbstractPersister<TerritoryTypeInput, TerritoryTypeEntity, TerritoryCategoryEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	TerritoryTypeRepository territoryTypeRepository;

	@Override
	protected void validateForm(final TerritoryTypeInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected TerritoryCategoryEntity loadContext(final TerritoryTypeInput form) {
		final GroupEntity group = groupRepository.load(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		for(final TerritoryCategoryEntity territoryCategory : group.getWh40kSkirmishRules().getTerritoryCategories()) {
			if(territoryCategory.hasId(form.getTerritoryCategoryId())) {
				return territoryCategory;
			}
		}
		throw new DataNotFoundException(String.format("Territory category %d (group %d)", form.getTerritoryCategoryId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsContext(
			final TerritoryTypeInput form,
			final BindingResult bindingResult,
			final TerritoryCategoryEntity context) {
		for(final TerritoryCategoryEntity territoryCategory : context.getRules().getTerritoryCategories()) {
			for(final TerritoryTypeEntity territoryType : territoryCategory.getTerritoryTypes()) {
				if(territoryType.getName().equals(form.getName())
						&& ! territoryType.hasId(form.getId())) {
					form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
				}
			}
		}
	}

	@Override
	protected boolean isCreateNew(final TerritoryTypeInput form) {
		return !form.isPersistent();
	}

	@Override
	protected TerritoryTypeEntity loadExisting(final TerritoryTypeInput form, final TerritoryCategoryEntity context) {
		for(final TerritoryTypeEntity territoryType : context.getTerritoryTypes()) {
			if(territoryType.hasId(form.getId())) {
				return territoryType;
			}
		}
		throw new DataNotFoundException(String.format("Territory type %d (territory category %d, group %d)", form.getId(), form.getTerritoryCategoryId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final TerritoryTypeInput form,
			final BindingResult bindingResult,
			final TerritoryTypeEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit territory type %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected TerritoryTypeEntity createNew(final TerritoryTypeInput form, final TerritoryCategoryEntity context) {
		return new TerritoryTypeEntity(context);
	}

	@Override
	protected void updateValues(final TerritoryTypeInput form, final TerritoryTypeEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected TerritoryTypeEntity makePersistent(final TerritoryTypeEntity entity) {
		return territoryTypeRepository.save(entity);
	}
}

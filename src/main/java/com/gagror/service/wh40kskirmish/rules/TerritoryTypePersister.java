package com.gagror.service.wh40kskirmish.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeEntity;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeInput;
import com.gagror.data.wh40kskirmish.rules.territory.TerritoryTypeRepository;
import com.gagror.service.AbstractIdentifiablePersister;

@Service
public class TerritoryTypePersister
extends AbstractIdentifiablePersister<TerritoryTypeInput, TerritoryTypeEntity, TerritoryCategoryEntity> {

	@Autowired
	TerritoryCategoryRepository territoryCategoryRepository;

	@Autowired
	TerritoryTypeRepository territoryTypeRepository;

	@Override
	protected void validateForm(final TerritoryTypeInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected TerritoryCategoryEntity loadContext(final TerritoryTypeInput form) {
		return territoryCategoryRepository.load(form.getGroupId(), form.getTerritoryCategoryId());
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
	protected TerritoryTypeEntity loadExisting(final TerritoryTypeInput form, final TerritoryCategoryEntity context) {
		for(final TerritoryTypeEntity territoryType : context.getTerritoryTypes()) {
			if(territoryType.hasId(form.getId())) {
				return territoryType;
			}
		}
		throw new DataNotFoundException(String.format("Territory type %d (territory category %d, group %d)", form.getId(), form.getTerritoryCategoryId(), form.getGroupId()));
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
		return territoryTypeRepository.persist(entity);
	}
}

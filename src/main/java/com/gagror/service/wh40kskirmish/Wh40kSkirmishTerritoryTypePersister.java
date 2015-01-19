package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeEntity;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeInput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishTerritoryTypePersister
extends AbstractPersister<Wh40kSkirmishTerritoryTypeInput, Wh40kSkirmishTerritoryTypeEntity, Wh40kSkirmishTerritoryCategoryEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishTerritoryTypeRepository territoryTypeRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishTerritoryTypeInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishTerritoryCategoryEntity loadContext(final Wh40kSkirmishTerritoryTypeInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new IllegalStateException(String.format("Group %s lacks WH40K skirmish rules", group));
		}
		for(final Wh40kSkirmishTerritoryCategoryEntity territoryCategory : group.getWh40kSkirmishRules().getTerritoryCategories()) {
			if(territoryCategory.getId().equals(form.getTerritoryCategoryId())) {
				return territoryCategory;
			}
		}
		throw new IllegalStateException(String.format("Group %s does not have territory category %d", group, form.getTerritoryCategoryId()));
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishTerritoryTypeInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishTerritoryTypeEntity loadExisting(final Wh40kSkirmishTerritoryTypeInput form, final Wh40kSkirmishTerritoryCategoryEntity context) {
		for(final Wh40kSkirmishTerritoryTypeEntity territoryType : context.getTerritoryTypes()) {
			if(territoryType.getId().equals(form.getId())) {
				return territoryType;
			}
		}
		throw new IllegalStateException(String.format("Failed to find territory type %d when editing", form.getId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishTerritoryTypeInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishTerritoryTypeEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit territory type %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishTerritoryTypeEntity createNew(final Wh40kSkirmishTerritoryTypeInput form, final Wh40kSkirmishTerritoryCategoryEntity context) {
		return new Wh40kSkirmishTerritoryTypeEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishTerritoryTypeInput form, final Wh40kSkirmishTerritoryTypeEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected Wh40kSkirmishTerritoryTypeEntity makePersistent(final Wh40kSkirmishTerritoryTypeEntity entity) {
		return territoryTypeRepository.save(entity);
	}
}

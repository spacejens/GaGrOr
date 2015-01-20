package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemTypeEntity;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemTypeInput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemTypeRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishItemTypePersister
extends AbstractPersister<Wh40kSkirmishItemTypeInput, Wh40kSkirmishItemTypeEntity, Wh40kSkirmishItemCategoryEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishItemTypeRepository itemTypeRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishItemTypeInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishItemCategoryEntity loadContext(final Wh40kSkirmishItemTypeInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		for(final Wh40kSkirmishItemCategoryEntity itemCategory : group.getWh40kSkirmishRules().getItemCategories()) {
			if(itemCategory.getId().equals(form.getItemCategoryId())) {
				return itemCategory;
			}
		}
		throw new IllegalStateException(String.format("Group %s does not have item category %d", group, form.getItemCategoryId()));
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishItemTypeInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishItemTypeEntity loadExisting(final Wh40kSkirmishItemTypeInput form, final Wh40kSkirmishItemCategoryEntity context) {
		for(final Wh40kSkirmishItemTypeEntity itemType : context.getItemTypes()) {
			if(itemType.getId().equals(form.getId())) {
				return itemType;
			}
		}
		throw new IllegalStateException(String.format("Failed to find item type %d when editing", form.getId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishItemTypeInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishItemTypeEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit item type %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishItemTypeEntity createNew(final Wh40kSkirmishItemTypeInput form, final Wh40kSkirmishItemCategoryEntity context) {
		return new Wh40kSkirmishItemTypeEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishItemTypeInput form, final Wh40kSkirmishItemTypeEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected Wh40kSkirmishItemTypeEntity makePersistent(final Wh40kSkirmishItemTypeEntity entity) {
		return itemTypeRepository.save(entity);
	}
}

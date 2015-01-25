package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeInput;
import com.gagror.data.wh40kskirmish.rules.items.ItemTypeRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class ItemTypePersister
extends AbstractPersister<ItemTypeInput, ItemTypeEntity, ItemCategoryEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	ItemTypeRepository itemTypeRepository;

	@Override
	protected void validateForm(final ItemTypeInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected ItemCategoryEntity loadContext(final ItemTypeInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		for(final ItemCategoryEntity itemCategory : group.getWh40kSkirmishRules().getItemCategories()) {
			if(itemCategory.getId().equals(form.getItemCategoryId())) {
				return itemCategory;
			}
		}
		throw new DataNotFoundException(String.format("Item category %d (group %d)", form.getItemCategoryId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsContext(
			final ItemTypeInput form,
			final BindingResult bindingResult,
			final ItemCategoryEntity context) {
		for(final ItemCategoryEntity itemCategory : context.getRules().getItemCategories()) {
			for(final ItemTypeEntity itemType : itemCategory.getItemTypes()) {
				if(itemType.getName().equals(form.getName())
						&& ! itemType.getId().equals(form.getId())) {
					form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
				}
			}
		}
	}

	@Override
	protected boolean isCreateNew(final ItemTypeInput form) {
		return null == form.getId();
	}

	@Override
	protected ItemTypeEntity loadExisting(final ItemTypeInput form, final ItemCategoryEntity context) {
		for(final ItemTypeEntity itemType : context.getItemTypes()) {
			if(itemType.getId().equals(form.getId())) {
				return itemType;
			}
		}
		throw new DataNotFoundException(String.format("Item type %d (item category %d, group %d)", form.getId(), form.getItemCategoryId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final ItemTypeInput form,
			final BindingResult bindingResult,
			final ItemTypeEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit item type %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected ItemTypeEntity createNew(final ItemTypeInput form, final ItemCategoryEntity context) {
		return new ItemTypeEntity(context);
	}

	@Override
	protected void updateValues(final ItemTypeInput form, final ItemTypeEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected ItemTypeEntity makePersistent(final ItemTypeEntity entity) {
		return itemTypeRepository.save(entity);
	}
}

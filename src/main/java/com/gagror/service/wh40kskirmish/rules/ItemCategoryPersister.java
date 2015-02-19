package com.gagror.service.wh40kskirmish.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryInput;
import com.gagror.data.wh40kskirmish.rules.items.ItemCategoryRepository;
import com.gagror.service.AbstractIdentifiablePersister;

@Service
public class ItemCategoryPersister
extends AbstractIdentifiablePersister<ItemCategoryInput, ItemCategoryEntity, Wh40kSkirmishRulesEntity> {

	@Autowired
	RulesRepository rulesRepository;

	@Autowired
	ItemCategoryRepository itemCategoryRepository;

	@Override
	protected void validateForm(final ItemCategoryInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadContext(final ItemCategoryInput form) {
		return rulesRepository.load(form.getGroupId());
	}

	@Override
	protected void validateFormVsContext(
			final ItemCategoryInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishRulesEntity context) {
		for(final ItemCategoryEntity itemCategory : context.getItemCategories()) {
			if(itemCategory.getName().equals(form.getName())
					&& ! itemCategory.hasId(form.getId())) {
				form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
			}
		}
	}

	@Override
	protected ItemCategoryEntity loadExisting(final ItemCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		for(final ItemCategoryEntity itemCategory : context.getItemCategories()) {
			if(itemCategory.hasId(form.getId())) {
				return itemCategory;
			}
		}
		throw new DataNotFoundException(String.format("Item category %d (group %d)", form.getId(), form.getGroupId()));
	}

	@Override
	protected ItemCategoryEntity createNew(final ItemCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		return new ItemCategoryEntity(context);
	}

	@Override
	protected void updateValues(final ItemCategoryInput form, final ItemCategoryEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected ItemCategoryEntity makePersistent(final ItemCategoryEntity entity) {
		return itemCategoryRepository.persist(entity);
	}
}

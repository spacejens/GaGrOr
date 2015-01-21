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
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryInput;
import com.gagror.data.wh40kskirmish.rules.items.Wh40kSkirmishItemCategoryRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishItemCategoryPersister
extends AbstractPersister<Wh40kSkirmishItemCategoryInput, Wh40kSkirmishItemCategoryEntity, Wh40kSkirmishRulesEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishItemCategoryRepository itemCategoryRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishItemCategoryInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadContext(final Wh40kSkirmishItemCategoryInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishItemCategoryInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishItemCategoryEntity loadExisting(final Wh40kSkirmishItemCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		for(final Wh40kSkirmishItemCategoryEntity itemCategory : context.getItemCategories()) {
			if(itemCategory.getId().equals(form.getId())) {
				return itemCategory;
			}
		}
		throw new DataNotFoundException(String.format("Item category %d (group %d)", form.getId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishItemCategoryInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishItemCategoryEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit item category %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishItemCategoryEntity createNew(final Wh40kSkirmishItemCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		return new Wh40kSkirmishItemCategoryEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishItemCategoryInput form, final Wh40kSkirmishItemCategoryEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected Wh40kSkirmishItemCategoryEntity makePersistent(final Wh40kSkirmishItemCategoryEntity entity) {
		return itemCategoryRepository.save(entity);
	}
}

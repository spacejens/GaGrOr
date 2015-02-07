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
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryInput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class SkillCategoryPersister
extends AbstractPersister<SkillCategoryInput, SkillCategoryEntity, Wh40kSkirmishRulesEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	SkillCategoryRepository skillCategoryRepository;

	@Override
	protected void validateForm(final SkillCategoryInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadContext(final SkillCategoryInput form) {
		final GroupEntity group = groupRepository.load(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}

	@Override
	protected void validateFormVsContext(
			final SkillCategoryInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishRulesEntity context) {
		for(final SkillCategoryEntity skillCategory : context.getSkillCategories()) {
			if(skillCategory.getName().equals(form.getName())
					&& ! skillCategory.hasId(form.getId())) {
				form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
			}
		}
	}

	@Override
	protected boolean isCreateNew(final SkillCategoryInput form) {
		return !form.isPersistent();
	}

	@Override
	protected SkillCategoryEntity loadExisting(final SkillCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		for(final SkillCategoryEntity skillCategory : context.getSkillCategories()) {
			if(skillCategory.hasId(form.getId())) {
				return skillCategory;
			}
		}
		throw new DataNotFoundException(String.format("Skill category %d (group %d)", form.getId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final SkillCategoryInput form,
			final BindingResult bindingResult,
			final SkillCategoryEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit skill category %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected SkillCategoryEntity createNew(final SkillCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		return new SkillCategoryEntity(context);
	}

	@Override
	protected void updateValues(final SkillCategoryInput form, final SkillCategoryEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected SkillCategoryEntity makePersistent(final SkillCategoryEntity entity) {
		return skillCategoryRepository.persist(entity);
	}
}

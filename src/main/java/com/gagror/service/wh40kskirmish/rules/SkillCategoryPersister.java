package com.gagror.service.wh40kskirmish.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryInput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryRepository;
import com.gagror.service.AbstractIdentifiablePersister;

@Service
public class SkillCategoryPersister
extends AbstractIdentifiablePersister<SkillCategoryInput, SkillCategoryEntity, Wh40kSkirmishRulesEntity> {

	@Autowired
	RulesRepository rulesRepository;

	@Autowired
	SkillCategoryRepository skillCategoryRepository;

	@Override
	protected void validateForm(final SkillCategoryInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadContext(final SkillCategoryInput form) {
		return rulesRepository.load(form.getGroupId());
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
	protected SkillCategoryEntity loadExisting(final SkillCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		for(final SkillCategoryEntity skillCategory : context.getSkillCategories()) {
			if(skillCategory.hasId(form.getId())) {
				return skillCategory;
			}
		}
		throw new DataNotFoundException(String.format("Skill category %d (group %d)", form.getId(), form.getGroupId()));
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

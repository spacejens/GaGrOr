package com.gagror.service.wh40kskirmish.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryRepository;
import com.gagror.data.wh40kskirmish.rules.skills.SkillEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillInput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillRepository;
import com.gagror.service.AbstractIdentifiablePersister;

@Service
public class SkillPersister
extends AbstractIdentifiablePersister<SkillInput, SkillEntity, SkillCategoryEntity> {

	@Autowired
	SkillCategoryRepository skillCategoryRepository;

	@Autowired
	SkillRepository skillRepository;

	@Override
	protected void validateForm(final SkillInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected SkillCategoryEntity loadContext(final SkillInput form) {
		return skillCategoryRepository.load(form.getGroupId(), form.getSkillCategoryId());
	}

	@Override
	protected void validateFormVsContext(
			final SkillInput form,
			final BindingResult bindingResult,
			final SkillCategoryEntity context) {
		for(final SkillCategoryEntity skillCategory : context.getRules().getSkillCategories()) {
			for(final SkillEntity skill : skillCategory.getSkills()) {
				if(skill.getName().equals(form.getName())
						&& ! skill.hasId(form.getId())) {
					form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
				}
			}
		}
	}

	@Override
	protected SkillEntity loadExisting(final SkillInput form, final SkillCategoryEntity context) {
		for(final SkillEntity skill : context.getSkills()) {
			if(skill.hasId(form.getId())) {
				return skill;
			}
		}
		throw new DataNotFoundException(String.format("Skill %d (skill category %d, group %d)", form.getId(), form.getSkillCategoryId(), form.getGroupId()));
	}

	@Override
	protected SkillEntity createNew(final SkillInput form, final SkillCategoryEntity context) {
		return new SkillEntity(context);
	}

	@Override
	protected void updateValues(final SkillInput form, final SkillEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected SkillEntity makePersistent(final SkillEntity entity) {
		return skillRepository.persist(entity);
	}
}

package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.skills.SkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillEntity;
import com.gagror.data.wh40kskirmish.rules.skills.SkillInput;
import com.gagror.data.wh40kskirmish.rules.skills.SkillRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class SkillPersister
extends AbstractPersister<SkillInput, SkillEntity, SkillCategoryEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	SkillRepository skillRepository;

	@Override
	protected void validateForm(final SkillInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected SkillCategoryEntity loadContext(final SkillInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		for(final SkillCategoryEntity skillCategory : group.getWh40kSkirmishRules().getSkillCategories()) {
			if(skillCategory.getId().equals(form.getSkillCategoryId())) {
				return skillCategory;
			}
		}
		throw new DataNotFoundException(String.format("Skill category %d (group %d)", form.getSkillCategoryId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsContext(
			final SkillInput form,
			final BindingResult bindingResult,
			final SkillCategoryEntity context) {
		for(final SkillCategoryEntity skillCategory : context.getRules().getSkillCategories()) {
			for(final SkillEntity skill : skillCategory.getSkills()) {
				if(skill.getName().equals(form.getName())
						&& ! skill.getId().equals(form.getId())) {
					form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
				}
			}
		}
	}

	@Override
	protected boolean isCreateNew(final SkillInput form) {
		return null == form.getId();
	}

	@Override
	protected SkillEntity loadExisting(final SkillInput form, final SkillCategoryEntity context) {
		for(final SkillEntity skill : context.getSkills()) {
			if(skill.getId().equals(form.getId())) {
				return skill;
			}
		}
		throw new DataNotFoundException(String.format("Skill %d (skill category %d, group %d)", form.getId(), form.getSkillCategoryId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final SkillInput form,
			final BindingResult bindingResult,
			final SkillEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit skill %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
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
		return skillRepository.save(entity);
	}
}

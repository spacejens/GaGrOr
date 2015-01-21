package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryInput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishSkillCategoryPersister
extends AbstractPersister<Wh40kSkirmishSkillCategoryInput, Wh40kSkirmishSkillCategoryEntity, Wh40kSkirmishRulesEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishSkillCategoryRepository skillCategoryRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishSkillCategoryInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadContext(final Wh40kSkirmishSkillCategoryInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishSkillCategoryInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishSkillCategoryEntity loadExisting(final Wh40kSkirmishSkillCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		for(final Wh40kSkirmishSkillCategoryEntity skillCategory : context.getSkillCategories()) {
			if(skillCategory.getId().equals(form.getId())) {
				return skillCategory;
			}
		}
		throw new DataNotFoundException(String.format("Skill category %d (group %d)", form.getId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishSkillCategoryInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishSkillCategoryEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit skill category %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishSkillCategoryEntity createNew(final Wh40kSkirmishSkillCategoryInput form, final Wh40kSkirmishRulesEntity context) {
		return new Wh40kSkirmishSkillCategoryEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishSkillCategoryInput form, final Wh40kSkirmishSkillCategoryEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected Wh40kSkirmishSkillCategoryEntity makePersistent(final Wh40kSkirmishSkillCategoryEntity entity) {
		return skillCategoryRepository.save(entity);
	}
}

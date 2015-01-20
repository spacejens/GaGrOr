package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillCategoryEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillEntity;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillInput;
import com.gagror.data.wh40kskirmish.rules.skills.Wh40kSkirmishSkillRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishSkillPersister
extends AbstractPersister<Wh40kSkirmishSkillInput, Wh40kSkirmishSkillEntity, Wh40kSkirmishSkillCategoryEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishSkillRepository skillRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishSkillInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected Wh40kSkirmishSkillCategoryEntity loadContext(final Wh40kSkirmishSkillInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new IllegalStateException(String.format("Group %s lacks WH40K skirmish rules", group));
		}
		for(final Wh40kSkirmishSkillCategoryEntity skillCategory : group.getWh40kSkirmishRules().getSkillCategories()) {
			if(skillCategory.getId().equals(form.getSkillCategoryId())) {
				return skillCategory;
			}
		}
		throw new IllegalStateException(String.format("Group %s does not have skill category %d", group, form.getSkillCategoryId()));
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishSkillInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishSkillEntity loadExisting(final Wh40kSkirmishSkillInput form, final Wh40kSkirmishSkillCategoryEntity context) {
		for(final Wh40kSkirmishSkillEntity skill : context.getSkills()) {
			if(skill.getId().equals(form.getId())) {
				return skill;
			}
		}
		throw new IllegalStateException(String.format("Failed to find skill %d when editing", form.getId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishSkillInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishSkillEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit skill %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishSkillEntity createNew(final Wh40kSkirmishSkillInput form, final Wh40kSkirmishSkillCategoryEntity context) {
		return new Wh40kSkirmishSkillEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishSkillInput form, final Wh40kSkirmishSkillEntity entity) {
		entity.setName(form.getName());
	}

	@Override
	protected Wh40kSkirmishSkillEntity makePersistent(final Wh40kSkirmishSkillEntity entity) {
		return skillRepository.save(entity);
	}
}

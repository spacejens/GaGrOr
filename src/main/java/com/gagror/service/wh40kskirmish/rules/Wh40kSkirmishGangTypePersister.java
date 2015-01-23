package com.gagror.service.wh40kskirmish.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.group.WrongGroupTypeException;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.experience.Wh40kSkirmishExperiencePointsComparator;
import com.gagror.data.wh40kskirmish.rules.experience.Wh40kSkirmishExperienceLevelEntity;
import com.gagror.data.wh40kskirmish.rules.experience.Wh40kSkirmishExperienceLevelInput;
import com.gagror.data.wh40kskirmish.rules.experience.Wh40kSkirmishExperienceLevelRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishGangTypePersister
extends AbstractPersister<Wh40kSkirmishGangTypeInput, Wh40kSkirmishGangTypeEntity, Wh40kSkirmishRulesEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	Wh40kSkirmishGangTypeRepository gangTypeRepository;

	@Autowired
	Wh40kSkirmishExperienceLevelRepository experienceLevelRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishGangTypeInput form, final BindingResult bindingResult) {
		/* It seems simple to sort the experience levels here, but it breaks error message binding.
		 * Instead, extract the experience points values from all input.
		 */
		final Set<Integer> experiencePoints = new HashSet<>();
		for(final Wh40kSkirmishExperienceLevelInput experienceLevel : form.getExperienceLevels()) {
			experiencePoints.add(experienceLevel.getExperiencePoints());
		}
		if(! experiencePoints.contains(0)) {
			form.addErrorExperienceLevelsMustStartAtZero(bindingResult);
		}
		if(experiencePoints.size() < form.getExperienceLevels().size()) {
			form.addErrorExperienceLevelsMustBeUnique(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadContext(final Wh40kSkirmishGangTypeInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishGangTypeInput form) {
		return null == form.getId();
	}

	@Override
	protected Wh40kSkirmishGangTypeEntity loadExisting(final Wh40kSkirmishGangTypeInput form, final Wh40kSkirmishRulesEntity context) {
		for(final Wh40kSkirmishGangTypeEntity gangType : context.getGangTypes()) {
			if(gangType.getId().equals(form.getId())) {
				return gangType;
			}
		}
		throw new DataNotFoundException(String.format("Gang type %d (group %d)", form.getId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishGangTypeInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishGangTypeEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit gang type %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected Wh40kSkirmishGangTypeEntity createNew(final Wh40kSkirmishGangTypeInput form, final Wh40kSkirmishRulesEntity context) {
		return new Wh40kSkirmishGangTypeEntity(context);
	}

	@Override
	protected void updateValues(final Wh40kSkirmishGangTypeInput form, final Wh40kSkirmishGangTypeEntity entity) {
		entity.setName(form.getName());
		// Experience levels
		final List<Wh40kSkirmishExperienceLevelEntity> entityExperienceLevels = new ArrayList<>(entity.getExperienceLevels());
		Collections.sort(entityExperienceLevels, Wh40kSkirmishExperiencePointsComparator.getInstance());
		for(int index=0 ; index < form.getExperienceLevels().size() ; index++) {
			final Wh40kSkirmishExperienceLevelInput inputExperienceLevel = form.getExperienceLevels().get(index);
			final Wh40kSkirmishExperienceLevelEntity entityExperienceLevel;
			if(index < entityExperienceLevels.size()) {
				// Update existing experience level
				entityExperienceLevel = entityExperienceLevels.get(index);
			} else {
				// Create new experience level
				entityExperienceLevel = new Wh40kSkirmishExperienceLevelEntity(entity);
			}
			entityExperienceLevel.setName(inputExperienceLevel.getName());
			entityExperienceLevel.setExperiencePoints(inputExperienceLevel.getExperiencePoints());
		}
		// Remove experience levels
		if(form.getExperienceLevels().size() < entityExperienceLevels.size()) {
			for(int index = form.getExperienceLevels().size() ; index < entityExperienceLevels.size() ; index++) {
				final Wh40kSkirmishExperienceLevelEntity entityExperienceLevel = entityExperienceLevels.get(index);
				entity.getExperienceLevels().remove(entityExperienceLevel);
				experienceLevelRepository.delete(entityExperienceLevel);
			}
		}
	}

	@Override
	protected Wh40kSkirmishGangTypeEntity makePersistent(final Wh40kSkirmishGangTypeEntity entity) {
		return gangTypeRepository.save(entity);
	}

	@Override
	protected void postPersistenceUpdate(final Wh40kSkirmishGangTypeEntity entity) {
		for(final Wh40kSkirmishExperienceLevelEntity experienceLevel : entity.getExperienceLevels()) {
			if(! experienceLevel.isPersistent()) {
				experienceLevelRepository.save(experienceLevel);
			}
		}
	}
}

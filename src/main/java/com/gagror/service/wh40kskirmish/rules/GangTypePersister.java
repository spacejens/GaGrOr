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
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelEntity;
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelInput;
import com.gagror.data.wh40kskirmish.rules.experience.ExperienceLevelRepository;
import com.gagror.data.wh40kskirmish.rules.experience.ExperiencePointsComparator;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class GangTypePersister
extends AbstractPersister<GangTypeInput, GangTypeEntity, Wh40kSkirmishRulesEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	GangTypeRepository gangTypeRepository;

	@Autowired
	ExperienceLevelRepository experienceLevelRepository;

	@Override
	protected void validateForm(final GangTypeInput form, final BindingResult bindingResult) {
		/* It seems simple to sort the experience levels here, but it breaks error message binding.
		 * Instead, extract the experience points values from all input.
		 */
		final Set<Integer> experiencePoints = new HashSet<>();
		for(final ExperienceLevelInput experienceLevel : form.getExperienceLevels()) {
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
	protected Wh40kSkirmishRulesEntity loadContext(final GangTypeInput form) {
		final GroupEntity group = groupRepository.load(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new WrongGroupTypeException(group);
		}
		return group.getWh40kSkirmishRules();
	}

	@Override
	protected void validateFormVsContext(
			final GangTypeInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishRulesEntity context) {
		for(final GangTypeEntity gangType : context.getGangTypes()) {
			if(gangType.getName().equals(form.getName())
					&& ! gangType.hasId(form.getId())) {
				form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
			}
		}
	}

	@Override
	protected boolean isCreateNew(final GangTypeInput form) {
		return !form.isPersistent();
	}

	@Override
	protected GangTypeEntity loadExisting(final GangTypeInput form, final Wh40kSkirmishRulesEntity context) {
		for(final GangTypeEntity gangType : context.getGangTypes()) {
			if(gangType.hasId(form.getId())) {
				return gangType;
			}
		}
		throw new DataNotFoundException(String.format("Gang type %d (group %d)", form.getId(), form.getGroupId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final GangTypeInput form,
			final BindingResult bindingResult,
			final GangTypeEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit gang type %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected GangTypeEntity createNew(final GangTypeInput form, final Wh40kSkirmishRulesEntity context) {
		return new GangTypeEntity(context);
	}

	@Override
	protected void updateValues(final GangTypeInput form, final GangTypeEntity entity) {
		entity.setName(form.getName());
		// Experience levels
		final List<ExperienceLevelEntity> entityExperienceLevels = new ArrayList<>(entity.getExperienceLevels());
		Collections.sort(entityExperienceLevels, ExperiencePointsComparator.getInstance());
		for(int index=0 ; index < form.getExperienceLevels().size() ; index++) {
			final ExperienceLevelInput inputExperienceLevel = form.getExperienceLevels().get(index);
			final ExperienceLevelEntity entityExperienceLevel;
			if(index < entityExperienceLevels.size()) {
				// Update existing experience level
				entityExperienceLevel = entityExperienceLevels.get(index);
			} else {
				// Create new experience level
				entityExperienceLevel = new ExperienceLevelEntity(entity);
			}
			entityExperienceLevel.setName(inputExperienceLevel.getName());
			entityExperienceLevel.setExperiencePoints(inputExperienceLevel.getExperiencePoints());
		}
		// Remove experience levels
		if(form.getExperienceLevels().size() < entityExperienceLevels.size()) {
			for(int index = form.getExperienceLevels().size() ; index < entityExperienceLevels.size() ; index++) {
				final ExperienceLevelEntity entityExperienceLevel = entityExperienceLevels.get(index);
				entity.getExperienceLevels().remove(entityExperienceLevel);
				experienceLevelRepository.delete(entityExperienceLevel);
			}
		}
	}

	@Override
	protected GangTypeEntity makePersistent(final GangTypeEntity entity) {
		return gangTypeRepository.persist(entity);
	}

	@Override
	protected void postPersistenceUpdate(final GangTypeEntity entity) {
		for(final ExperienceLevelEntity experienceLevel : entity.getExperienceLevels()) {
			if(! experienceLevel.isPersistent()) {
				experienceLevelRepository.persist(experienceLevel);
			}
		}
	}
}

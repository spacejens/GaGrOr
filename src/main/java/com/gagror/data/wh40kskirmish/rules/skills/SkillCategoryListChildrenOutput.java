package com.gagror.data.wh40kskirmish.rules.skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class SkillCategoryListChildrenOutput
extends SkillCategoryReferenceOutput {

	@Getter
	private final List<SkillReferenceOutput> skills;

	public SkillCategoryListChildrenOutput(final SkillCategoryEntity entity) {
		super(entity);
		// Sorted list of skills
		final List<SkillReferenceOutput> tempSkills = new ArrayList<>();
		for(final SkillEntity skill : entity.getSkills()) {
			tempSkills.add(new SkillReferenceOutput(skill));
		}
		Collections.sort(tempSkills);
		skills = Collections.unmodifiableList(tempSkills);
	}
}

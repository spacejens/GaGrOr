package com.gagror.data.wh40kskirmish.rules.skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class Wh40kSkirmishSkillCategoryListChildrenOutput
extends Wh40kSkirmishSkillCategoryReferenceOutput {

	@Getter
	private final List<Wh40kSkirmishSkillReferenceOutput> skills;

	public Wh40kSkirmishSkillCategoryListChildrenOutput(final Wh40kSkirmishSkillCategoryEntity entity) {
		super(entity);
		// Sorted list of skills
		final List<Wh40kSkirmishSkillReferenceOutput> tempSkills = new ArrayList<>();
		for(final Wh40kSkirmishSkillEntity skill : entity.getSkills()) {
			tempSkills.add(new Wh40kSkirmishSkillReferenceOutput(skill));
		}
		Collections.sort(tempSkills);
		skills = Collections.unmodifiableList(tempSkills);
	}
}

package com.gagror.data.wh40kskirmish.rules.skills;

import lombok.Getter;

public class SkillOutput extends SkillReferenceOutput {

	@Getter
	private final SkillCategoryOutput skillCategory;

	public SkillOutput(
			final SkillEntity entity,
			final SkillCategoryOutput skillCategory) {
		super(entity);
		this.skillCategory = skillCategory;
	}
}

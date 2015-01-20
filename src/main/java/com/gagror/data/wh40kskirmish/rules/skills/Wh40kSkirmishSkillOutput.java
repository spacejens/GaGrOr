package com.gagror.data.wh40kskirmish.rules.skills;

import lombok.Getter;

public class Wh40kSkirmishSkillOutput extends Wh40kSkirmishSkillReferenceOutput {

	@Getter
	private final Wh40kSkirmishSkillCategoryOutput skillCategory;

	public Wh40kSkirmishSkillOutput(
			final Wh40kSkirmishSkillEntity entity,
			final Wh40kSkirmishSkillCategoryOutput skillCategory) {
		super(entity);
		this.skillCategory = skillCategory;
	}
}

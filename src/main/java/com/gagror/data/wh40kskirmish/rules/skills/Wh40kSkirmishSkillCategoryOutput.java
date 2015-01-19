package com.gagror.data.wh40kskirmish.rules.skills;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;

public class Wh40kSkirmishSkillCategoryOutput extends Wh40kSkirmishSkillCategoryReferenceOutput {

	@Getter
	private final GroupReferenceOutput group;

	public Wh40kSkirmishSkillCategoryOutput(final Wh40kSkirmishSkillCategoryEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
	}
}

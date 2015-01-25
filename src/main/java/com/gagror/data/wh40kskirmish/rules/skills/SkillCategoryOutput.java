package com.gagror.data.wh40kskirmish.rules.skills;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;

public class SkillCategoryOutput extends SkillCategoryReferenceOutput {

	@Getter
	private final GroupReferenceOutput group;

	public SkillCategoryOutput(final SkillCategoryEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
	}
}

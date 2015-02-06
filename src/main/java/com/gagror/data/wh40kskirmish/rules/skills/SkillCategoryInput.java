package com.gagror.data.wh40kskirmish.rules.skills;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class SkillCategoryInput
extends AbstractIdentifiableNamedInput<SkillCategoryOutput> {

	@Getter
	@Setter
	private Long groupId;

	public SkillCategoryInput(final SkillCategoryOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
	}

	public SkillCategoryInput(final Long groupId) {
		setGroupId(groupId);
	}
}

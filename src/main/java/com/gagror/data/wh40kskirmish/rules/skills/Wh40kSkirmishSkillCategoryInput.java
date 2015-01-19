package com.gagror.data.wh40kskirmish.rules.skills;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishSkillCategoryInput
extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishSkillCategoryOutput> {

	@Getter
	@Setter
	private Long groupId;

	public Wh40kSkirmishSkillCategoryInput(final Wh40kSkirmishSkillCategoryOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
	}

	public Wh40kSkirmishSkillCategoryInput(final Long groupId) {
		setGroupId(groupId);
	}
}

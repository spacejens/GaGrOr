package com.gagror.data.wh40kskirmish.rules.skills;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishSkillInput extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishSkillOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long skillCategoryId;

	public Wh40kSkirmishSkillInput(final Wh40kSkirmishSkillOutput currentState) {
		super(currentState);
		setGroupId(currentState.getSkillCategory().getGroup().getId());
		setSkillCategoryId(currentState.getSkillCategory().getId());
	}

	public Wh40kSkirmishSkillInput(final Long groupId, final Long skillCategoryId) {
		setGroupId(groupId);
		setSkillCategoryId(skillCategoryId);
	}
}

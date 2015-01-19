package com.gagror.data.wh40kskirmish.rules.items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishItemCategoryInput
extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishItemCategoryOutput> {

	@Getter
	@Setter
	private Long groupId;

	public Wh40kSkirmishItemCategoryInput(final Wh40kSkirmishItemCategoryOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
	}

	public Wh40kSkirmishItemCategoryInput(final Long groupId) {
		setGroupId(groupId);
	}
}

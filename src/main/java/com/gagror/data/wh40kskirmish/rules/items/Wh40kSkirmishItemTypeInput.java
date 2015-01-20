package com.gagror.data.wh40kskirmish.rules.items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishItemTypeInput extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishItemTypeOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long itemCategoryId;

	public Wh40kSkirmishItemTypeInput(final Wh40kSkirmishItemTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getItemCategory().getGroup().getId());
		setItemCategoryId(currentState.getItemCategory().getId());
	}

	public Wh40kSkirmishItemTypeInput(final Long groupId, final Long itemCategoryId) {
		setGroupId(groupId);
		setItemCategoryId(itemCategoryId);
	}
}

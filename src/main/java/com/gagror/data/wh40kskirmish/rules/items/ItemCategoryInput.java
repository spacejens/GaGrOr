package com.gagror.data.wh40kskirmish.rules.items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class ItemCategoryInput
extends AbstractIdentifiableNamedInput<ItemCategoryOutput> {

	@Getter
	@Setter
	private Long groupId;

	public ItemCategoryInput(final ItemCategoryOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
	}

	public ItemCategoryInput(final Long groupId) {
		setGroupId(groupId);
	}
}

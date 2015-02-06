package com.gagror.data.wh40kskirmish.rules.items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.group.GroupIdentifiable;

@NoArgsConstructor
public class ItemCategoryInput
extends AbstractIdentifiableNamedInput<ItemCategoryOutput>
implements GroupIdentifiable {

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

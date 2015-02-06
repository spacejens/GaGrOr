package com.gagror.data.wh40kskirmish.rules.items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.group.GroupIdentifiable;

@NoArgsConstructor
public class ItemTypeInput
extends AbstractIdentifiableNamedInput<ItemTypeOutput>
implements GroupIdentifiable {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long itemCategoryId;

	public ItemTypeInput(final ItemTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getItemCategory().getGroup().getId());
		setItemCategoryId(currentState.getItemCategory().getId());
	}

	public ItemTypeInput(final Long groupId, final Long itemCategoryId) {
		setGroupId(groupId);
		setItemCategoryId(itemCategoryId);
	}
}

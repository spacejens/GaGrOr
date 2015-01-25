package com.gagror.data.wh40kskirmish.rules.items;

import lombok.Getter;

public class ItemTypeOutput extends ItemTypeReferenceOutput {

	@Getter
	private final ItemCategoryOutput itemCategory;

	public ItemTypeOutput(
			final ItemTypeEntity entity,
			final ItemCategoryOutput itemCategory) {
		super(entity);
		this.itemCategory = itemCategory;
	}
}

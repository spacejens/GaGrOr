package com.gagror.data.wh40kskirmish.rules.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class ItemCategoryListChildrenOutput
extends ItemCategoryReferenceOutput {

	@Getter
	private final List<ItemTypeReferenceOutput> itemTypes;

	public ItemCategoryListChildrenOutput(final ItemCategoryEntity entity) {
		super(entity);
		// Sorted list of item types
		final List<ItemTypeReferenceOutput> tempItemTypes = new ArrayList<>();
		for(final ItemTypeEntity itemType : entity.getItemTypes()) {
			tempItemTypes.add(new ItemTypeReferenceOutput(itemType));
		}
		Collections.sort(tempItemTypes);
		itemTypes = Collections.unmodifiableList(tempItemTypes);
	}
}

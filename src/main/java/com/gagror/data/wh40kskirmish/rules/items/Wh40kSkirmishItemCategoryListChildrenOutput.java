package com.gagror.data.wh40kskirmish.rules.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class Wh40kSkirmishItemCategoryListChildrenOutput
extends Wh40kSkirmishItemCategoryReferenceOutput {

	@Getter
	private final List<Wh40kSkirmishItemTypeReferenceOutput> itemTypes;

	public Wh40kSkirmishItemCategoryListChildrenOutput(final Wh40kSkirmishItemCategoryEntity entity) {
		super(entity);
		// Sorted list of item types
		final List<Wh40kSkirmishItemTypeReferenceOutput> tempItemTypes = new ArrayList<>();
		for(final Wh40kSkirmishItemTypeEntity itemType : entity.getItemTypes()) {
			tempItemTypes.add(new Wh40kSkirmishItemTypeReferenceOutput(itemType));
		}
		Collections.sort(tempItemTypes);
		itemTypes = Collections.unmodifiableList(tempItemTypes);
	}
}

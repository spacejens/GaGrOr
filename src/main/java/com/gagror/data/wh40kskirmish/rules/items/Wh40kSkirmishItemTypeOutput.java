package com.gagror.data.wh40kskirmish.rules.items;

import lombok.Getter;

public class Wh40kSkirmishItemTypeOutput extends Wh40kSkirmishItemTypeReferenceOutput {

	@Getter
	private final Wh40kSkirmishItemCategoryOutput itemCategory;

	public Wh40kSkirmishItemTypeOutput(
			final Wh40kSkirmishItemTypeEntity entity,
			final Wh40kSkirmishItemCategoryOutput itemCategory) {
		super(entity);
		this.itemCategory = itemCategory;
	}
}

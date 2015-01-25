package com.gagror.data.wh40kskirmish.rules.items;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;

public class ItemCategoryOutput extends ItemCategoryReferenceOutput {

	@Getter
	private final GroupReferenceOutput group;

	public ItemCategoryOutput(final ItemCategoryEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
	}
}

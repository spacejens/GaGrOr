package com.gagror.data.wh40kskirmish.rules.items;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;

public class Wh40kSkirmishItemCategoryOutput extends Wh40kSkirmishItemCategoryReferenceOutput {

	@Getter
	private final GroupReferenceOutput group;

	public Wh40kSkirmishItemCategoryOutput(final Wh40kSkirmishItemCategoryEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
	}
}

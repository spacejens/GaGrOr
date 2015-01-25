package com.gagror.data.wh40kskirmish.rules.territory;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;

public class TerritoryCategoryOutput extends TerritoryCategoryReferenceOutput {

	@Getter
	private final GroupReferenceOutput group;

	public TerritoryCategoryOutput(final TerritoryCategoryEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
	}
}

package com.gagror.data.wh40kskirmish.rules.territory;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;

public class Wh40kSkirmishTerritoryCategoryOutput extends Wh40kSkirmishTerritoryCategoryReferenceOutput {

	@Getter
	private final GroupReferenceOutput group;

	public Wh40kSkirmishTerritoryCategoryOutput(final Wh40kSkirmishTerritoryCategoryEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
	}
}

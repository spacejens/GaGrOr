package com.gagror.data.wh40kskirmish;

import lombok.Getter;

import com.gagror.data.group.GroupReferenceOutput;

public class Wh40kSkirmishFactionOutput extends Wh40kSkirmishFactionReferenceOutput {

	@Getter
	private GroupReferenceOutput group;

	@Getter
	private Wh40kSkirmishGangTypeReferenceOutput gangType;

	public Wh40kSkirmishFactionOutput(
			final Wh40kSkirmishFactionEntity entity,
			final GroupReferenceOutput group,
			final Wh40kSkirmishGangTypeReferenceOutput gangType) {
		super(entity);
		this.group = group;
		this.gangType = gangType;
	}
}

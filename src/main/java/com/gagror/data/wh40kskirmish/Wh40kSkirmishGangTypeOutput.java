package com.gagror.data.wh40kskirmish;

import lombok.Getter;

import com.gagror.data.AbstractEntityOutput;

public class Wh40kSkirmishGangTypeOutput
extends AbstractEntityOutput
implements Comparable<Wh40kSkirmishGangTypeOutput> {

	@Getter
	private final String name;

	public Wh40kSkirmishGangTypeOutput(final Wh40kSkirmishGangTypeEntity entity) {
		super(entity);
		name = entity.getName();
	}

	@Override
	public int compareTo(final Wh40kSkirmishGangTypeOutput o) {
		return name.compareTo(o.getName());
	}
}

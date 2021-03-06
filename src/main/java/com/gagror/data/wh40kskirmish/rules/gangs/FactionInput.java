package com.gagror.data.wh40kskirmish.rules.gangs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.group.GroupIdentifiable;

@NoArgsConstructor
public class FactionInput
extends AbstractIdentifiableNamedInput<FactionOutput>
implements GroupIdentifiable {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long gangTypeId;

	public FactionInput(final FactionOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGangType().getGroup().getId());
		setGangTypeId(currentState.getGangType().getId());
	}

	public FactionInput(final Long groupId, final Long gangTypeId) {
		setGroupId(groupId);
		setGangTypeId(gangTypeId);
	}
}

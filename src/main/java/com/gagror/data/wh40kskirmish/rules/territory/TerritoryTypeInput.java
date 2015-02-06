package com.gagror.data.wh40kskirmish.rules.territory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.group.GroupIdentifiable;

@NoArgsConstructor
public class TerritoryTypeInput
extends AbstractIdentifiableNamedInput<TerritoryTypeOutput>
implements GroupIdentifiable {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long territoryCategoryId;

	public TerritoryTypeInput(final TerritoryTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getTerritoryCategory().getGroup().getId());
		setTerritoryCategoryId(currentState.getTerritoryCategory().getId());
	}

	public TerritoryTypeInput(final Long groupId, final Long territoryCategoryId) {
		setGroupId(groupId);
		setTerritoryCategoryId(territoryCategoryId);
	}
}

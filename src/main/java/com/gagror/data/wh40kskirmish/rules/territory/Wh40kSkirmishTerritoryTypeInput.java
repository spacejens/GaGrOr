package com.gagror.data.wh40kskirmish.rules.territory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;

@NoArgsConstructor
public class Wh40kSkirmishTerritoryTypeInput extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishTerritoryTypeOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	private Long territoryCategoryId;

	public Wh40kSkirmishTerritoryTypeInput(final Wh40kSkirmishTerritoryTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getTerritoryCategory().getGroup().getId());
		setTerritoryCategoryId(currentState.getTerritoryCategory().getId());
	}

	public Wh40kSkirmishTerritoryTypeInput(final Long groupId, final Long territoryCategoryId) {
		setGroupId(groupId);
		setTerritoryCategoryId(territoryCategoryId);
	}
}

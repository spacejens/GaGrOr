package com.gagror.data.wh40kskirmish.rules.territory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.group.GroupIdentifiable;

@NoArgsConstructor
public class TerritoryCategoryInput
extends AbstractIdentifiableNamedInput<TerritoryCategoryOutput>
implements GroupIdentifiable {

	@Getter
	@Setter
	private Long groupId;

	public TerritoryCategoryInput(final TerritoryCategoryOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
	}

	public TerritoryCategoryInput(final Long groupId) {
		setGroupId(groupId);
	}
}

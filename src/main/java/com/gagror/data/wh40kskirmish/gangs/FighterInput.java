package com.gagror.data.wh40kskirmish.gangs;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.group.GroupIdentifiable;

@NoArgsConstructor
public class FighterInput
extends AbstractIdentifiableNamedInput<FighterEditOutput>
implements GroupIdentifiable {
	// TODO All input that implements GroupIdentifiable should share a superclass that extracts the groupId

	@Getter
	@Setter
	@NotNull
	private Long groupId;

	@Getter
	@Setter
	@NotNull
	private Long gangId;

	public FighterInput(final FighterEditOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
		setGangId(currentState.getGang().getId());
	}
}

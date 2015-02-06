package com.gagror.data.wh40kskirmish.gangs;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.group.GroupIdentifiable;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;

@NoArgsConstructor
public class GangInput
extends AbstractIdentifiableNamedInput<GangOutput>
implements GroupIdentifiable {

	@Getter
	@Setter
	@NotNull
	private Long groupId;

	@Getter
	@Setter
	private Long gangTypeId;

	@Getter
	@Setter
	@NotNull
	private Long factionId;

	@Getter
	@Setter
	@NotNull
	private Long playerId;

	@Getter
	@Setter
	@Min(0)
	private int money;

	public GangInput(final RulesOutput rules) {
		setGroupId(rules.getGroup().getId());
		setMoney(rules.getStartingMoney());
	}

	public GangInput(final GangOutput currentState) {
		super(currentState);
		setGroupId(currentState.getRules().getGroup().getId());
		setGangTypeId(currentState.getGangType().getId());
		setFactionId(currentState.getFaction().getId());
		setPlayerId(currentState.getPlayer().getId());
		setMoney(currentState.getMoney());
	}
}

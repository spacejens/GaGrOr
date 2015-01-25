package com.gagror.data.wh40kskirmish.gangs;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;

@NoArgsConstructor
public class GangInput extends AbstractIdentifiableNamedInput<Long, GangOutput> {

	@Getter
	@Setter
	@NotNull
	private Long groupId;

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

	// TODO Constructor to create edit gang form from current state

	public GangInput(final RulesOutput rules) {
		setGroupId(rules.getGroup().getId());
		setMoney(rules.getStartingMoney());
	}
}

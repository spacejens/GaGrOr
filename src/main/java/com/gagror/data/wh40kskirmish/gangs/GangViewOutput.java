package com.gagror.data.wh40kskirmish.gangs;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.wh40kskirmish.rules.RulesOutput;

public class GangViewOutput extends GangOutput {

	@Getter
	private final List<FighterViewOutput> fighters;

	public GangViewOutput(final GangEntity entity, final RulesOutput rules, final List<FighterViewOutput> fighters) {
		super(entity, rules);
		this.fighters = Collections.unmodifiableList(fighters);
	}
}

package com.gagror.data.wh40kskirmish.gangs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeReferenceOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.RaceEntity;

public class GangRecruitOutput extends GangOutput {

	@Getter
	private final List<FighterTypeReferenceOutput> fighterTypes;

	public GangRecruitOutput(final GangEntity entity, final RulesOutput rules) {
		super(entity, rules);
		// Get all available fighter types
		final List<FighterTypeReferenceOutput> tempFighterTypes = new ArrayList<>();
		for(final RaceEntity race : entity.getFaction().getGangType().getRaces()) {
			for(final FighterTypeEntity fighterType : race.getFighterTypes()) {
				tempFighterTypes.add(new FighterTypeReferenceOutput(fighterType));
			}
		}
		// TODO Show cost of fighter types on recruitment page
		// TODO Show and use maximum number of various fighter types on recruitment page
		Collections.sort(tempFighterTypes);
		fighterTypes = Collections.unmodifiableList(tempFighterTypes);
	}
}

package com.gagror.data.wh40kskirmish.gangs;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.gagror.data.Output;
import com.gagror.data.group.GroupViewMembersOutput;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;

@RequiredArgsConstructor
public class CreateGangOutput implements Output {

	@Getter
	private final GroupViewMembersOutput group;

	@Getter
	private final RulesOutput rules;

	@Getter
	private final List<FactionReferenceOutput> factions;
}

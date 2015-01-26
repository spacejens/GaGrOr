package com.gagror.data.wh40kskirmish.gangs;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.gagror.data.Output;
import com.gagror.data.group.GroupViewMembersOutput;
import com.gagror.data.wh40kskirmish.rules.RulesOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionReferenceOutput;

@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public class EditGangOutput implements Output {

	@Getter
	private final GroupViewMembersOutput group;

	@Getter
	private final RulesOutput rules;

	@Getter
	private final List<FactionReferenceOutput> factions;

	@Getter
	private final GangOutput currentState;

	public static EditGangOutput createGang(
			final GroupViewMembersOutput group,
			final RulesOutput rules,
			final List<FactionReferenceOutput> factions) {
		return new EditGangOutput(group, rules, factions, null);
	}

	public static EditGangOutput editGang(
			final GroupViewMembersOutput group,
			final GangOutput currentState) {
		return new EditGangOutput(group, currentState.getRules(), null, currentState);
	}
}

package com.gagror.data.wh40kskirmish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangListOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;

public class GroupOutput extends GroupReferenceOutput {

	@Getter
	private final List<GangListOutput> gangs;

	public GroupOutput(final GroupEntity group) {
		super(group);
		gangs = listGangs(group);
	}

	private static List<GangListOutput> listGangs(final GroupEntity group) {
		final List<GangListOutput> gangs = new ArrayList<>();
		for(final GangTypeEntity gangType : group.getWh40kSkirmishRules().getGangTypes()) {
			for(final FactionEntity faction : gangType.getFactions()) {
				for(final GangEntity gang : faction.getGangs()) {
					gangs.add(new GangListOutput(gang));
				}
			}
		}
		Collections.sort(gangs);
		return Collections.unmodifiableList(gangs);
	}
}

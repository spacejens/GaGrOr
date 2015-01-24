package com.gagror.data.wh40kskirmish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupMemberEntity;
import com.gagror.data.group.GroupReferenceOutput;
import com.gagror.data.wh40kskirmish.gangs.Wh40kSkirmishGangEntity;
import com.gagror.data.wh40kskirmish.gangs.Wh40kSkirmishGangListOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeEntity;

public class Wh40kSkirmishGroupOutput extends GroupReferenceOutput {

	@Getter
	private final List<Wh40kSkirmishGangListOutput> gangs;

	public Wh40kSkirmishGroupOutput(final GroupEntity group) {
		super(group);
		gangs = listGangs(group);
	}

	public Wh40kSkirmishGroupOutput(final GroupMemberEntity membership) {
		super(membership);
		gangs = listGangs(membership.getGroup());
	}

	private static List<Wh40kSkirmishGangListOutput> listGangs(final GroupEntity group) {
		final List<Wh40kSkirmishGangListOutput> gangs = new ArrayList<>();
		for(final Wh40kSkirmishGangTypeEntity gangType : group.getWh40kSkirmishRules().getGangTypes()) {
			for(final Wh40kSkirmishFactionEntity faction : gangType.getFactions()) {
				for(final Wh40kSkirmishGangEntity gang : faction.getGangs()) {
					gangs.add(new Wh40kSkirmishGangListOutput(gang));
				}
			}
		}
		Collections.sort(gangs);
		return Collections.unmodifiableList(gangs);
	}
}

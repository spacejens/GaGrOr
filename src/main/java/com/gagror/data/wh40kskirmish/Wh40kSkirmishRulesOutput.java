package com.gagror.data.wh40kskirmish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import com.gagror.data.AbstractEditableEntityOutput;
import com.gagror.data.group.GroupReferenceOutput;

public class Wh40kSkirmishRulesOutput
extends AbstractEditableEntityOutput {

	@Getter
	private final GroupReferenceOutput group;

	@Getter
	private final List<Wh40kSkirmishGangTypeListChildrenOutput> gangTypes;

	public Wh40kSkirmishRulesOutput(final Wh40kSkirmishRulesEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
		// Sorted list of gang types and their factions, races, and fighter types
		final List<Wh40kSkirmishGangTypeListChildrenOutput> tempGangTypes = new ArrayList<>();
		for(final Wh40kSkirmishGangTypeEntity gangType : entity.getGangTypes()) {
			tempGangTypes.add(new Wh40kSkirmishGangTypeListChildrenOutput(gangType));
		}
		Collections.sort(tempGangTypes);
		gangTypes = Collections.unmodifiableList(tempGangTypes);
	}
}

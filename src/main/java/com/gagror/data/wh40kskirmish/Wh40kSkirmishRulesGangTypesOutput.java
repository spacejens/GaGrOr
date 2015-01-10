package com.gagror.data.wh40kskirmish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

public class Wh40kSkirmishRulesGangTypesOutput extends Wh40kSkirmishRulesOutput {

	@Getter
	private final List<Wh40kSkirmishGangTypeReferenceOutput> gangTypes;

	public Wh40kSkirmishRulesGangTypesOutput(final Wh40kSkirmishRulesEntity entity) {
		super(entity);
		final List<Wh40kSkirmishGangTypeReferenceOutput> tempGangTypes = new ArrayList<>();
		for(final Wh40kSkirmishGangTypeEntity gangType : entity.getGangTypes()) {
			tempGangTypes.add(new Wh40kSkirmishGangTypeReferenceOutput(gangType));
		}
		Collections.sort(tempGangTypes);
		gangTypes = Collections.unmodifiableList(tempGangTypes);
	}
}

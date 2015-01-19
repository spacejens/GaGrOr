package com.gagror.data.wh40kskirmish.rules;

import lombok.Getter;

import com.gagror.data.AbstractEditableEntityOutput;
import com.gagror.data.group.GroupReferenceOutput;

public class Wh40kSkirmishRulesOutput
extends AbstractEditableEntityOutput {

	@Getter
	private final GroupReferenceOutput group;

	public Wh40kSkirmishRulesOutput(final Wh40kSkirmishRulesEntity entity, final GroupReferenceOutput group) {
		super(entity);
		this.group = group;
	}
}

package com.gagror.data.group;

import lombok.Getter;

import com.gagror.data.AbstractEditableNamedEntityOutput;
import com.gagror.data.URLOutput;

public class GroupReferenceOutput
extends AbstractEditableNamedEntityOutput
implements URLOutput {

	@Getter
	private final GroupType groupType;

	@Getter
	private final boolean viewableByAnyone;

	public GroupReferenceOutput(final GroupEntity group) {
		super(group);
		groupType = group.getGroupType();
		viewableByAnyone = group.isViewableByAnyone();
	}

	@Override
	public String getUrl() {
		return String.format("%s/%d", getGroupType().getGroupUrl(), getId());
	}
}

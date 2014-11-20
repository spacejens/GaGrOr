package com.gagror.data.group;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.gagror.data.EnumIdMapping;
import com.gagror.data.Identifiable;
import com.gagror.data.PropertyNameDisplayable;

@RequiredArgsConstructor
public enum GroupType implements Identifiable<Integer>, PropertyNameDisplayable {

	SOCIAL(0);

	private static final EnumIdMapping<Integer, GroupType> IDMAP = new EnumIdMapping<>(GroupType.class);

	@Getter
	private final Integer id;

	@Override
	public String getDisplayNameProperty() {
		return String.format("group.type.%s", name());
	}

	public static GroupType fromId(final Integer id) {
		return IDMAP.fromId(id);
	}
}

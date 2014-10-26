package com.gagror.data.group;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.gagror.data.EnumIdMapping;
import com.gagror.data.Identifiable;
import com.gagror.data.PropertyNameDisplayable;

@RequiredArgsConstructor
public enum MemberType implements Identifiable<Integer>, PropertyNameDisplayable {

	INVITED(0, false, false, true),
	MEMBER(1, true, false, false),
	OWNER(2, true, true, false);

	private static final EnumIdMapping<Integer, MemberType> IDMAP = new EnumIdMapping<>(MemberType.class);

	@Getter
	private final Integer id;

	@Getter
	private final boolean member;

	@Getter
	private final boolean owner;

	@Getter
	private final boolean invitation;

	@Override
	public String getDisplayNameProperty() {
		return String.format("member.type.%s", name());
	}

	public static MemberType fromId(final Integer id) {
		return IDMAP.fromId(id);
	}
}

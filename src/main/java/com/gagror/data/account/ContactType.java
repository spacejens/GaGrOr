package com.gagror.data.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.gagror.data.EnumIdMapping;
import com.gagror.data.Identifiable;
import com.gagror.data.PropertyNameDisplayable;

@RequiredArgsConstructor
public enum ContactType implements Identifiable<Integer>, PropertyNameDisplayable {

	BLOCKED(-1),
	NEUTRAL(0),
	REQUESTED(1),
	AUTOMATIC(2),
	APPROVED(3);

	private static final EnumIdMapping<Integer, ContactType> IDMAP = new EnumIdMapping<>(ContactType.class);

	@Getter
	private final Integer id;

	@Override
	public String getDisplayNameProperty() {
		return String.format("contact.type.%s", name());
	}

	public static ContactType fromId(final Integer id) {
		return IDMAP.fromId(id);
	}
}

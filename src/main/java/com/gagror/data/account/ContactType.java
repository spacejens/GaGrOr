package com.gagror.data.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.gagror.data.EnumIdMapping;
import com.gagror.data.Identifiable;
import com.gagror.data.PropertyNameDisplayable;

@RequiredArgsConstructor
public enum ContactType implements Identifiable<Integer>, PropertyNameDisplayable {

	BLOCKED(0, false, false), // TODO Support blocking other accounts
	REQUESTED(1, false, true),
	AUTOMATIC(2, true, false), // TODO Add all group members as contacts automatically when joining
	APPROVED(3, true, false);

	private static final EnumIdMapping<Integer, ContactType> IDMAP = new EnumIdMapping<>(ContactType.class);

	@Getter
	private final Integer id;

	@Getter
	private final boolean contact;

	@Getter
	private final boolean request;

	@Override
	public String getDisplayNameProperty() {
		return String.format("contact.type.%s", name());
	}

	public static ContactType fromId(final Integer id) {
		return IDMAP.fromId(id);
	}
}

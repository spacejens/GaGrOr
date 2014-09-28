package com.gagror.data.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AccountType {

	STANDARD(0),
	SYSTEM_OWNER(1);

	@Getter
	private final int id;

	public static AccountType fromId(final int id) {
		// TODO Move enum fromId logic to a id-enum mapping from a utility class
		for(final AccountType type : AccountType.values()) {
			if(id == type.getId()) {
				return type;
			}
		}
		throw new IllegalArgumentException(String.format("Unknown id: %d", id));
	}

	public boolean isStandard() {
		return STANDARD == this;
	}

	public boolean isSystemOwner() {
		return SYSTEM_OWNER == this;
	}
}

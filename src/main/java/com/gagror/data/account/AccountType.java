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
		for(final AccountType type : AccountType.values()) {
			if(id == type.getId()) {
				return type;
			}
		}
		throw new IllegalArgumentException(String.format("Unknown id: %d", id));
	}
}

package com.gagror.data.account;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public enum AccountType {

	STANDARD(0),
	SYSTEM_OWNER(1,
			SecurityRoles.ROLE_ADMIN);

	@Getter
	private final int id;

	/**
	 * Sorted as needed by {@link UserDetails#getAuthorities()}.
	 */
	@Getter
	private final Collection<GrantedAuthority> authorities;

	private AccountType(final int id, final String... roles) {
		this.id = id;
		Arrays.sort(roles);
		final List<GrantedAuthority> tempAuthorities = new ArrayList<>();
		for(final String role : roles) {
			tempAuthorities.add(new SimpleGrantedAuthority(role));
		}
		authorities = unmodifiableList(tempAuthorities);
	}

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

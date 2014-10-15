package com.gagror.data.account;

import static java.util.Collections.unmodifiableSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum AccountType {

	STANDARD(0,
			SecurityRoles.USER),
	SYSTEM_OWNER(1,
			SecurityRoles.USER,
			SecurityRoles.ADMIN);

	@Getter
	private final int id;

	@Getter
	private final Collection<GrantedAuthority> authorities;

	private AccountType(final int id, final String... roles) {
		this.id = id;
		final Set<GrantedAuthority> tempAuthorities = new HashSet<>();
		for(final String role : roles) {
			tempAuthorities.add(new SimpleGrantedAuthority(role));
		}
		authorities = unmodifiableSet(tempAuthorities);
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

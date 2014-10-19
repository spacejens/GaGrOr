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

import com.gagror.data.EnumIdMapping;
import com.gagror.data.Identifiable;

public enum AccountType implements Identifiable<Integer> {

	STANDARD(0, "fi-torso"),
	SYSTEM_OWNER(1, "fi-crown",
			SecurityRoles.ROLE_ADMIN);

	private static final EnumIdMapping<Integer, AccountType> IDMAP = new EnumIdMapping<>(AccountType.class);

	@Getter
	private final Integer id;

	@Getter
	private final String cssClass;

	/**
	 * Sorted as needed by {@link UserDetails#getAuthorities()}.
	 */
	@Getter
	private final Collection<GrantedAuthority> authorities;

	private AccountType(final Integer id, final String cssClass, final String... roles) {
		this.id = id;
		this.cssClass = cssClass;
		Arrays.sort(roles);
		final List<GrantedAuthority> tempAuthorities = new ArrayList<>();
		for(final String role : roles) {
			tempAuthorities.add(new SimpleGrantedAuthority(role));
		}
		authorities = unmodifiableList(tempAuthorities);
	}

	public static AccountType fromId(final Integer id) {
		return IDMAP.fromId(id);
	}

	public boolean isStandard() {
		return STANDARD == this;
	}

	public boolean isSystemOwner() {
		return SYSTEM_OWNER == this;
	}
}

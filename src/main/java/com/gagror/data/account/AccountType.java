package com.gagror.data.account;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gagror.data.EnumIdMapping;
import com.gagror.data.Identifiable;

public enum AccountType implements Identifiable<Integer> {

	STANDARD(0, "account.icon.standard"),
	ADMIN(2, "account.icon.admin",
			Arrays.asList(STANDARD), true,
			SecurityRoles.ROLE_ADMIN),
	SYSTEM_OWNER(1, "account.icon.systemowner",
			Arrays.asList(STANDARD, ADMIN), false,
			SecurityRoles.ROLE_ADMIN);

	private static final EnumIdMapping<Integer, AccountType> IDMAP = new EnumIdMapping<>(AccountType.class);

	@Getter
	private final Integer id;

	@Getter
	private final String cssClass;

	@Getter
	private final List<AccountType> mayEdit;

	/**
	 * Sorted as needed by {@link UserDetails#getAuthorities()}.
	 */
	@Getter
	private final Collection<GrantedAuthority> authorities;

	private AccountType(final Integer id, final String cssClass, final String... roles) {
		this(id, cssClass, Collections.<AccountType>emptyList(), false, roles);
	}

	private AccountType(final Integer id, final String cssClass, final List<AccountType> mayEdit, final boolean mayEditPeers, final String... roles) {
		this.id = id;
		this.cssClass = cssClass;
		// Finalize what account types may be edited
		final List<AccountType> mayEditTemp = new ArrayList<>();
		mayEditTemp.addAll(mayEdit);
		if(mayEditPeers) {
			mayEditTemp.add(this);
		}
		this.mayEdit = unmodifiableList(mayEditTemp);
		// Add authorities
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
}

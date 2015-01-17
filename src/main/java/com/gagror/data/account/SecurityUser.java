package com.gagror.data.account;

import java.util.Collection;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser implements UserDetails {

	@Getter
	private final String password;

	@Getter
	private final String username;

	@Getter
	private final boolean accountNonExpired;

	@Getter
	private final boolean accountNonLocked;

	@Getter
	private final boolean credentialsNonExpired;

	@Getter
	private final boolean enabled;

	@Getter
	private final Collection<GrantedAuthority> authorities;

	public SecurityUser(final AccountEntity account) {
		username = account.getName();
		password = account.getPassword();
		accountNonExpired = true;
		accountNonLocked = ! account.isLocked();
		credentialsNonExpired = true;
		enabled = account.isActive();
		authorities = account.getAccountType().getAuthorities();
	}
}

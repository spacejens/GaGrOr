package com.gagror.data.account;

import java.util.Collection;

import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class SecurityUser implements UserDetails {

	private final String password;
	private final String username;
	private final boolean accountNonExpired;
	private final boolean accountNonLocked;
	private final boolean credentialsNonExpired;
	private final boolean enabled;
	private final Collection<GrantedAuthority> authorities;

	public SecurityUser(final AccountEntity account) {
		username = account.getUsername();
		password = account.getPassword();
		// TODO Various security account flags need changing when data model supports them
		accountNonExpired = true;
		accountNonLocked = true;
		credentialsNonExpired = true;
		enabled = true;
		authorities = account.getAccountType().getAuthorities();
	}
}

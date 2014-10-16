package com.gagror.data.account;

public interface SecurityRoles {

	// TODO Remove user role, instead use generic isAuthenticated() EL expression
	public static final String ROLE_USER = "USER";
	public static final String ROLE_ADMIN = "ADMIN";

	public static final String IS_LOGGED_IN = "hasRole('"+ROLE_USER+"')";
	public static final String IS_ADMIN = "hasRole('"+ROLE_ADMIN+"')";
	public static final String IS_PUBLIC = "true";
	public static final String IS_NOT_LOGGED_IN = "isAnonymous()";
}

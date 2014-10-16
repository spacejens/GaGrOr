package com.gagror.data.account;

public interface SecurityRoles {

	public static final String ROLE_ADMIN = "ADMIN";

	public static final String IS_LOGGED_IN = "isAuthenticated()";
	public static final String IS_ADMIN = "hasRole('"+ROLE_ADMIN+"')";
	public static final String IS_PUBLIC = "true";
	public static final String IS_NOT_LOGGED_IN = "isAnonymous()";
}

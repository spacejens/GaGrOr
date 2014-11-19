package com.gagror.data.account;

public interface SecurityRoles {

	// TODO Keep only security roles in this interface, and put controller security expressions in the AbstractController (or in more specific controllers where appropriate)

	public static final String ROLE_ADMIN = "ADMIN";

	public static final String ATTR_CONTACT_ID = "contactId";
	public static final String ATTR_ACCOUNT_ID = "accountId";
	public static final String ATTR_GROUP_ID = "groupId";

	public static final String IS_LOGGED_IN = "isAuthenticated()";
	public static final String IS_ADMIN = "hasRole('"+ROLE_ADMIN+"')";
	public static final String IS_PUBLIC = "true";
	public static final String IS_NOT_LOGGED_IN = "isAnonymous()";

	public static final String HAS_CONTACT = IS_LOGGED_IN + " and hasPermission(#" + ATTR_CONTACT_ID + ", 'hasContact')";
	public static final String HAS_INCOMING_CONTACT_REQUEST = IS_LOGGED_IN + " and hasPermission(#" + ATTR_CONTACT_ID + ", 'hasContactRequest')";

	public static final String MAY_EDIT_ACCOUNT = IS_LOGGED_IN + " and hasPermission(#" + ATTR_ACCOUNT_ID + ", 'editAccount')";
	public static final String MAY_VIEW_GROUP = IS_LOGGED_IN + " and hasPermission(#" + ATTR_GROUP_ID + ", 'viewGroup')"; // TODO Group view permission should not require being logged in
	public static final String MAY_ADMIN_GROUP = IS_LOGGED_IN + " and hasPermission(#" + ATTR_GROUP_ID + ", 'adminGroup')";
}

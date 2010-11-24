package se.spacejens.gagror.view;

/**
 * Definitions for view parameters.
 * 
 * @author spacejens
 */
public enum ViewParameters {
	COMMON_LOGINFORM("loginForm"),
	PUBLIC_ERROR_HEADLINE("headline"),
	PUBLIC_ERROR_MESSAGE("message"),
	PUBLIC_LOGIN_HEADLINE("headline"),
	PUBLIC_LOGIN_MESSAGE("message"),
	PUBLIC_REGISTER_FORM("userRegistrationForm");

	/** Parameter name. */
	private final String name;

	/**
	 * Create instance.
	 * 
	 * @param name
	 *            Parameter name, not null.
	 */
	ViewParameters(final String name) {
		this.name = name;
	}

	/**
	 * Get parameter name.
	 * 
	 * @return Not null.
	 */
	public String getName() {
		return this.name;
	}
}

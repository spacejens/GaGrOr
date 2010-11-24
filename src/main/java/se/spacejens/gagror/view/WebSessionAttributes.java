package se.spacejens.gagror.view;

/**
 * Constants for those attributes stored in the web session.
 * 
 * @author spacejens
 */
public interface WebSessionAttributes {

	/** The username of the currently logged in user, or null. */
	public static final String USERNAME = "username";

	/** The encrypted password of the currently logged in user, or null. */
	public static final String PASSWORD = "password";
}

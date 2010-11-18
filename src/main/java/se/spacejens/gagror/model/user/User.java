package se.spacejens.gagror.model.user;

import se.spacejens.gagror.model.Entity;

/**
 * A user in the system.
 * 
 * @author spacejens
 */
public interface User extends Entity {

	/** Minimum length of valid username. */
	public static final int USERNAME_MIN_LENGTH = 2;

	/** Maximum length of valid username. */
	public static final int USERNAME_MAX_LENGTH = 16;

	/** Minimum length of password. */
	public static final int PASSWORD_MIN_LENGTH = 8;

	/** Maximum length of password. */
	public static final int PASSWORD_MAX_LENGTH = 32;

	/** Minimum length of encrypted password. */
	public static final int PASSWORD_ENCRYPTED_MIN_LENGTH = 28;

	/** Maximum length of encrypted password. */
	public static final int PASSWORD_ENCRYPTED_MAX_LENGTH = 28;

	/**
	 * Get the username.
	 * 
	 * @return Not null.
	 */
	String getUsername();

	/**
	 * Get the encrypted password.
	 * 
	 * @return Not null.
	 */
	String getPassword();

	/**
	 * Set the encrypted password.
	 * 
	 * @param password
	 *            Not null.
	 */
	void setPassword(final String password);
}

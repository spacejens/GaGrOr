package se.spacejens.gagror.view.spring;

import javax.validation.constraints.NotNull;

/**
 * Form backing bean used when logging in.
 * 
 * @author spacejens
 */
public class UserLoginForm {

	/** Username. */
	private String username;

	/** Plaintext password. */
	private String password;

	/**
	 * Get the username.
	 * 
	 * @return Username.
	 */
	@NotNull
	public String getUsername() {
		return this.username;
	}

	/**
	 * Set the username.
	 * 
	 * @param username
	 *            Username.
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * Get the password.
	 * 
	 * @return Password.
	 */
	@NotNull
	public String getPassword() {
		return this.password;
	}

	/**
	 * Set the password.
	 * 
	 * @param password
	 *            Password.
	 */
	public void setPassword(final String password) {
		this.password = password;
	}
}

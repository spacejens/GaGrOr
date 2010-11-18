package se.spacejens.gagror.view.spring;

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

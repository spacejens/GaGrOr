package se.spacejens.gagror.view.spring;

/**
 * Form backing bean used when registering a new user.
 * 
 * @author spacejens
 */
public class UserRegistrationForm extends UserLoginForm {

	/** Plaintext password repeated. */
	private String repeatPassword;

	/**
	 * Get repeated plaintext password.
	 * 
	 * @return Plaintext password.
	 */
	public String getRepeatPassword() {
		return this.repeatPassword;
	}

	/**
	 * Set repeated plaintext password.
	 * 
	 * @param repeatPassword
	 *            Plaintext password.
	 */
	public void setRepeatPassword(final String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
}

package se.spacejens.gagror.model.user;

/**
 * Container for an encrypted password.
 * 
 * @author spacejens
 */
public interface Password {

	/**
	 * Get the encrypted password.
	 * 
	 * @return Not null.
	 */
	public String getPassword();
}

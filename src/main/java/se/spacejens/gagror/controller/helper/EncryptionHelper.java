package se.spacejens.gagror.controller.helper;

/**
 * Specification for the helper responsible for encryption.
 * 
 * @author spacejens
 */
public interface EncryptionHelper {

	/**
	 * Encrypt the password for a user.
	 * 
	 * @param username
	 *            Username for the user (to ensure the same password is
	 *            differently encrypted for different users).
	 * @param password
	 *            Password to encrypt.
	 * @return Encrypted password.
	 */
	public String encryptPassword(final String username, final String password);
}

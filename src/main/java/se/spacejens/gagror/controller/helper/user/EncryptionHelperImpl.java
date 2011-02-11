package se.spacejens.gagror.controller.helper.user;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.iharder.Base64;
import se.spacejens.gagror.GagrorImplementationException;
import se.spacejens.gagror.SecurityConstants;
import se.spacejens.gagror.controller.helper.HelperSupport;
import se.spacejens.gagror.model.JpaContext;

/**
 * This helper implementation provides methods used for encrypting various data.
 * 
 * @author spacejens
 */
public final class EncryptionHelperImpl extends HelperSupport implements EncryptionHelper {

	/**
	 * Create instance.
	 * 
	 * @param jpa
	 *            JPA context to use.
	 */
	public EncryptionHelperImpl(final JpaContext jpa) {
		super(jpa);
	}

	/**
	 * Get the digest algorithm.
	 * 
	 * @return Not null.
	 */
	MessageDigest getMessageDigest() {
		try {
			return MessageDigest.getInstance(SecurityConstants.ALGORITHM);
		} catch (final NoSuchAlgorithmException e) {
			throw new GagrorImplementationException(e);
		}
	}

	/**
	 * Get the bytes representing the text in the chosen encoding.
	 * 
	 * @param text
	 *            Not null
	 * @return Not null.
	 */
	byte[] getBytes(final String text) {
		try {
			return text.getBytes(SecurityConstants.ENCODING);
		} catch (final UnsupportedEncodingException e) {
			throw new GagrorImplementationException(e);
		}
	}

	/**
	 * Encrypt the provided text.
	 * 
	 * @param text
	 *            Plaintext.
	 * @return Ciphertext.
	 */
	private String encryptText(final String text) {
		return Base64.encodeBytes(this.getMessageDigest().digest(this.getBytes(text)));
	}

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
	@Override
	public String encryptPassword(final String username, final String password) {
		return this.encryptText(username + SecurityConstants.USERNAME_PASSWORD_JOINING + password);
	}
}

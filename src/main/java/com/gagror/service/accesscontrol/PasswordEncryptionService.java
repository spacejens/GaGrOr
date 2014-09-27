package com.gagror.service.accesscontrol;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.gagror.data.account.LoginCredentialsInput;

@Service
public class PasswordEncryptionService {

	public static final int SALT_BYTES = 12; // 16 characters in Base64
	public static final int SALT_STRING_LENGTH = 16;

	private final SecureRandom secureRandom = new SecureRandom();
	private final Base64.Encoder base64Encoder = Base64.getEncoder();

	/**
	 * Encrypt the password in the provided login credentials.
	 *
	 * @param loginCredentials Will be updated with the encrypted password.
	 * @return The encrypted password.
	 */
	public String encrypt(final LoginCredentialsInput loginCredentials) {
		// If the password has already been encrypted, use it
		if(null != loginCredentials.getEncryptedPassword()) {
			return loginCredentials.getEncryptedPassword();
		}
		// Generate a salt for the encryption
		final byte[] salt = generateSalt();
		// Encrypt the password
		// TODO Use a hash of some kind for password encryption
		final String encryptedPassword =
				new StringBuilder(loginCredentials.getPassword()).
				reverse().
				toString();
		// Store and return the encrypted password
		loginCredentials.setSalt(base64Encoder.encodeToString(salt));
		loginCredentials.setEncryptedPassword(encryptedPassword);
		return encryptedPassword;
	}

	private byte[] generateSalt() {
		byte[] salt = new byte[SALT_BYTES];
		secureRandom.nextBytes(salt);
		return salt;
	}
}

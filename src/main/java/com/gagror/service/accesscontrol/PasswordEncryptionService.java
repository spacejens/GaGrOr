package com.gagror.service.accesscontrol;

import org.springframework.stereotype.Service;

import com.gagror.data.account.LoginCredentialsInput;

@Service
public class PasswordEncryptionService {

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
		// Encrypt the password
		// TODO Use a hash of some kind for password encryption
		final String encryptedPassword =
				new StringBuilder(loginCredentials.getPassword()).
				reverse().
				toString();
		// Store and return the encrypted password
		loginCredentials.setEncryptedPassword(encryptedPassword);
		return encryptedPassword;
	}
}

package com.gagror.service.accesscontrol;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagror.data.account.LoginCredentialsInput;

@Service
public class PasswordEncryptionService {

	private final MessageDigest sha256;

	@Autowired
	SecureRandomService secureRandom;

	@SneakyThrows(NoSuchAlgorithmException.class)
	public PasswordEncryptionService() {
		sha256 = MessageDigest.getInstance("SHA-256");
	}

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
		// Get the salt for the encryption
		final byte[] salt;
		if(null != loginCredentials.getSalt()) {
			salt = DatatypeConverter.parseBase64Binary(loginCredentials.getSalt());
		} else {
			salt = secureRandom.generateRandomSalt();
			loginCredentials.setSalt(DatatypeConverter.printBase64Binary(salt));
		}
		// Encrypt the password
		final String encryptedPassword =
				this.encodePassword(loginCredentials.getPassword(), salt);
		loginCredentials.setEncryptedPassword(encryptedPassword);
		return encryptedPassword;
	}

	@SneakyThrows(UnsupportedEncodingException.class)
	private String encodePassword(final String password, final byte[] salt) {
		final byte[] passwordBytes = password.getBytes("UTF-8");
		final byte[] inputBytes = new byte[salt.length + passwordBytes.length];
		System.arraycopy(salt, 0, inputBytes, 0, salt.length);
		System.arraycopy(passwordBytes, 0, inputBytes, salt.length, passwordBytes.length);
		return DatatypeConverter.printBase64Binary(sha256.digest(inputBytes));
	}
}

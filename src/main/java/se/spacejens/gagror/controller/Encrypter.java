package se.spacejens.gagror.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.iharder.Base64;
import se.spacejens.gagror.GagrorImplementationException;

/**
 * This class provides methods used for encrypting various data.
 * 
 * @author spacejens
 */
public final class Encrypter {

	/**
	 * Private constructor to enforce access only through static methods.
	 */
	private Encrypter() {
	}

	/**
	 * Get the digest algorithm.
	 * 
	 * @return Not null.
	 */
	static MessageDigest getMessageDigest() {
		try {
			return MessageDigest.getInstance("SHA");
		} catch (final NoSuchAlgorithmException e) {
			throw new GagrorImplementationException("Encrypter algorithm invalid", e);
		}
	}

	/**
	 * Get the bytes representing the text in the chosen encoding.
	 * 
	 * @param text
	 *            Not null
	 * @return Not null.
	 */
	static byte[] getBytes(final String text) {
		try {
			return text.getBytes("UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new GagrorImplementationException("Encrypter encoding invalid", e);
		}
	}

	/**
	 * Encrypt the provided text.
	 * 
	 * @param text
	 *            Plaintext.
	 * @return Ciphertext.
	 */
	private static String encryptText(final String text) {
		return Base64.encodeBytes(Encrypter.getMessageDigest().digest(Encrypter.getBytes(text)));
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
	public static String encryptPassword(final String username, final String password) {
		return Encrypter.encryptText(username + " " + password);
	}
}

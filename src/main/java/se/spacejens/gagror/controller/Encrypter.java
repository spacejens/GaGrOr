package se.spacejens.gagror.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.iharder.Base64;

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
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm could not be found. Verified by unit test
	 *             never to be thrown.
	 */
	static MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
		return MessageDigest.getInstance("SHA");
	}

	/**
	 * Get the bytes representing the text in the chosen encoding.
	 * 
	 * @param text
	 *            Not null
	 * @return Not null.
	 * @throws UnsupportedEncodingException
	 *             If the encoding was not supported. Verified by unit test
	 *             never to be thrown.
	 */
	static byte[] getBytes(final String text) throws UnsupportedEncodingException {
		return text.getBytes("UTF-8");
	}

	/**
	 * Encrypt the provided text.
	 * 
	 * @param text
	 *            Plaintext.
	 * @return Ciphertext.
	 */
	private static String encryptText(final String text) {
		try {
			return Base64.encodeBytes(Encrypter.getMessageDigest().digest(Encrypter.getBytes(text)));
		} catch (Exception e) {
			// Since unit tests verify that exceptions are not thrown, this
			// should never happen
			throw new RuntimeException("Encrypter failed unexpectedly", e);
		}
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

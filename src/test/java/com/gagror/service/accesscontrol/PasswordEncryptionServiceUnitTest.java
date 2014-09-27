package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.gagror.data.account.LoginCredentialsInput;

public class PasswordEncryptionServiceUnitTest {

	private static final String USERNAME = "admin";
	private static final String PASSWORD = "password";
	private static final String ENCRYPTED = "cbT97FvathBBkU4kLQGaMhiFD+ijGbBJ3eB2/hVaKm8=";
	private static final String SALT = "KyPVwKOMpZkRp1ZM";

	PasswordEncryptionService instance;
	LoginCredentialsInput input;

	@Test
	public void encrypt_ok() {
		final String output = instance.encrypt(input);
		assertEquals("Incorrect encryption", ENCRYPTED, output);
	}

	@Test
	public void encrypt_providedSaltNotChanged() {
		instance.encrypt(input);
		assertEquals("Provided salt overwritten", SALT, input.getSalt());
	}

	@Test
	public void encrypt_encryptedPasswordStored_providedSalt() {
		instance.encrypt(input);
		assertEquals("Encryption not stored", ENCRYPTED, input.getEncryptedPassword());
	}

	@Test
	public void encrypt_generatedSaltStored() {
		input.setSalt(null);
		instance.encrypt(input);
		assertNotNull("Generated salt not stored", input.getSalt());
	}

	@Test
	public void encrypt_encryptedPasswordStored_generatedSalt() {
		input.setSalt(null);
		instance.encrypt(input);
		// TODO Verify that the password and salt match (refactoring needed to break out randomness)
		assertNotNull("Encryption not stored", input.getEncryptedPassword());
	}

	@Test
	public void encrypt_alreadyEncrypted() {
		final String alreadyEncrypted = "Already encrypted";
		input.setEncryptedPassword(alreadyEncrypted);
		instance.encrypt(input);
		assertEquals("Encryption overwritten", alreadyEncrypted, input.getEncryptedPassword());
	}

	@Before
	public void setupInput() {
		input = new LoginCredentialsInput();
		input.setUsername(USERNAME);
		input.setPassword(PASSWORD);
		input.setSalt(SALT);
	}

	@Before
	public void setupInstance() {
		instance = new PasswordEncryptionService();
	}
}

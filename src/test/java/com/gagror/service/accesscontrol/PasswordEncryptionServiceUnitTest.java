package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gagror.data.account.LoginCredentialsInput;

@RunWith(MockitoJUnitRunner.class)
public class PasswordEncryptionServiceUnitTest {

	private static final String USERNAME = "admin";
	private static final String PASSWORD = "password";
	private static final String SALT = "KyPVwKOMpZkRp1ZM";
	private static final String ENCRYPTED = "cbT97FvathBBkU4kLQGaMhiFD+ijGbBJ3eB2/hVaKm8=";
	private static final String SALT_GENERATED = "lrNAPBql23zhmV7V";
	private static final String ENCRYPTED_GENERATED = "hZOmwLcEXP+cCpUjoCBDMxLNKchp4j9zx+mk66D363E=";

	PasswordEncryptionService instance;
	LoginCredentialsInput input;

	@Mock
	SecureRandomService secureRandom;

	@Test
	public void encrypt_encryptedPasswordOutputted() {
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
	public void encrypt_encryptedPasswordOutputted_generatedSalt() {
		input.setSalt(null);
		final String output = instance.encrypt(input);
		assertEquals("Incorrect encryption", ENCRYPTED_GENERATED, output);
	}

	@Test
	public void encrypt_generatedSaltStored() {
		input.setSalt(null);
		instance.encrypt(input);
		assertEquals("Generated salt not stored", SALT_GENERATED, input.getSalt());
	}

	@Test
	public void encrypt_encryptedPasswordStored_generatedSalt() {
		input.setSalt(null);
		instance.encrypt(input);
		assertEquals("Encryption not stored", ENCRYPTED_GENERATED, input.getEncryptedPassword());
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
	public void setupSecureRandom() {
		when(secureRandom.generateRandomSalt()).thenReturn(Base64.getDecoder().decode(SALT_GENERATED));
	}

	@Before
	public void setupInstance() {
		instance = new PasswordEncryptionService();
		instance.secureRandom = secureRandom;
	}
}

package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class SecureRandomServiceUnitTest {

	SecureRandomService instance;

	@Test
	public void generateRandomSalt_length() {
		assertEquals("Wrong salt length", SecureRandomService.SALT_BYTES, instance.generateRandomSalt().length);
	}

	@Test
	public void generateRandomSalt_noNullBytes() {
		for(final byte saltByte : instance.generateRandomSalt()) {
			assertNotNull("Null byte in generated salt", saltByte);
		}
	}

	@Before
	public void setupInstance() {
		instance = new SecureRandomService();
	}
}

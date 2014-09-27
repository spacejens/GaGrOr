package com.gagror.service.accesscontrol;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class SecureRandomService {

	public static final int SALT_BYTES = 12; // 16 characters in Base64

	private final SecureRandom secureRandom = new SecureRandom();

	public byte[] generateRandomSalt() {
		byte[] salt = new byte[SALT_BYTES];
		secureRandom.nextBytes(salt);
		return salt;
	}
}

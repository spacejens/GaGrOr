package com.gagror.service.accesscontrol;

import org.springframework.stereotype.Service;

import com.gagror.data.account.LoginCredentialsInput;

@Service
public class PasswordEncryptionService {

	public String encrypt(final LoginCredentialsInput loginCredentials) {
		// TODO Use a hash of some kind for password encryption
		return loginCredentials.getPassword();
	}
}

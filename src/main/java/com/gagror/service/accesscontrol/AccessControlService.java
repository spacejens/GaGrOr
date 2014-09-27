package com.gagror.service.accesscontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.LoginCredentialsInput;

@Service
@Transactional
public class AccessControlService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	PasswordEncryptionService passwordEncryption;

	public String logIn(final LoginCredentialsInput loginCredentials) {
		final AccountEntity account = accountRepository.findByLoginAndPassword(
				loginCredentials.getLogin(),
				passwordEncryption.encrypt(loginCredentials));
		// TODO Return output object?
		return null != account ? account.toString() : "NULL";
	}
}

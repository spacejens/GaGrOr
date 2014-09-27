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

	@Autowired
	SessionCredentialsComponent sessionCredentials;

	public String logIn(final LoginCredentialsInput loginCredentials) {
		if(null == loginCredentials) {
			return "NO CREDENTIALS";
		}
		final AccountEntity account = accountRepository.findByLoginAndPassword(
				loginCredentials.getLogin(),
				passwordEncryption.encrypt(loginCredentials));
		if(null != account) {
			sessionCredentials.setLoginCredentials(loginCredentials);
			// TODO Return output object?
			return account.toString();
		} else {
			return null;
		}
	}
}

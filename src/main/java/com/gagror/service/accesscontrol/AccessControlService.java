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

	@Autowired
	RequestAccountComponent requestAccount;

	public String getRequestAccount() {
		// TODO Return output object?
		if(! requestAccount.isLoaded()) {
			if(null != sessionCredentials.getLoginCredentials()) {
				requestAccount.setAccount(accountRepository.findByLoginAndPassword(
						sessionCredentials.getLoginCredentials().getLogin(),
						passwordEncryption.encrypt(sessionCredentials.getLoginCredentials())));
				requestAccount.setLoaded(true);
			} else {
				// No credentials, request account loading tried and failed
				requestAccount.setLoaded(true);
				return "NO CREDENTIALS";
			}
		}
		if(null != requestAccount.getAccount()) {
			return requestAccount.getAccount().toString();
		} else {
			return "NOT LOGGED IN";
		}
	}

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
			return "LOGIN FAILED";
		}
	}
}

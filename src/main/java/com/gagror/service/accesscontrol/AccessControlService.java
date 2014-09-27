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

	public AccountEntity getRequestAccountEntity() {
		if(! requestAccount.isLoaded()) {
			if(null != sessionCredentials.getLoginCredentials()) {
				requestAccount.setAccount(accountRepository.findByLoginAndPassword(
						sessionCredentials.getLoginCredentials().getLogin(),
						passwordEncryption.encrypt(sessionCredentials.getLoginCredentials())));
			} else {
				// No credentials, request account loading tried and failed
				requestAccount.setAccount(null);
			}
			requestAccount.setLoaded(true);
		}
		return requestAccount.getAccount();
	}

	public String getRequestAccount() {
		// TODO Return output object
		final AccountEntity account = getRequestAccountEntity();
		if(null != account) {
			return account.toString();
		} else {
			return "NOT LOGGED IN";
		}
	}

	public void logIn(final LoginCredentialsInput loginCredentials) {
		this.sessionCredentials.setLoginCredentials(loginCredentials);
	}
}

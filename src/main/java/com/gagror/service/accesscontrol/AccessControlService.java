package com.gagror.service.accesscontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.LoginCredentialsInput;
import com.gagror.data.account.RegisterInput;

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
				requestAccount.setAccount(accountRepository.findByUsernameAndPassword(
						sessionCredentials.getLoginCredentials().getUsername(),
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
			return account.getUsername();
		} else {
			return null;
		}
	}

	public void logIn(final LoginCredentialsInput loginCredentials) {
		this.sessionCredentials.setLoginCredentials(loginCredentials);
	}

	public void logOut() {
		this.sessionCredentials.setLoginCredentials(null);
	}

	public void register(final RegisterInput registerForm) {
		// Verify that the account can be created
		if(null != accountRepository.findByUsername(registerForm.getUsername())) {
			return;
		}
		if(! registerForm.getPassword().equals(registerForm.getPasswordRepeat())) {
			return;
		}
		// Create the account
		passwordEncryption.encrypt(registerForm);
		accountRepository.save(new AccountEntity(registerForm));
		// Automatically log in the newly registered user
		this.logIn(new LoginCredentialsInput(registerForm));
	}
}

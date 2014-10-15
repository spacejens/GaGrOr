package com.gagror.service.accesscontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountReferenceOutput;
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
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(null == authentication) {
			return null;
		}
		final String username = authentication.getName();
		return accountRepository.findByUsername(username);
	}

	public AccountReferenceOutput getRequestAccount() {
		final AccountEntity account = getRequestAccountEntity();
		if(null != account) {
			return new AccountReferenceOutput(account);
		} else {
			return null;
		}
	}

	public AccessControlResultType logIn(final LoginCredentialsInput loginCredentials) {
		requestAccount.setLoaded(false);
		this.sessionCredentials.setLoginCredentials(loginCredentials);
		if(null != getRequestAccountEntity()) {
			return AccessControlResultType.LOGGED_IN;
		} else {
			return AccessControlResultType.LOGIN_FAILED;
		}
	}

	public void logOut() {
		this.sessionCredentials.setLoginCredentials(null);
		this.requestAccount.setLoaded(false);
	}

	public AccessControlResultType register(final RegisterInput registerForm) {
		// Verify that the account can be created
		if(null != accountRepository.findByUsername(registerForm.getUsername())) {
			return AccessControlResultType.REGISTER_FAILED_USERNAME_BUSY;
		}
		if(! registerForm.getPassword().equals(registerForm.getPasswordRepeat())) {
			return AccessControlResultType.REGISTER_FAILED_PASSWORDS_DONT_MATCH;
		}
		// Create the account
		passwordEncryption.encrypt(registerForm);
		accountRepository.save(new AccountEntity(registerForm));
		// Automatically log in the newly registered user
		return logIn(new LoginCredentialsInput(registerForm));
	}
}

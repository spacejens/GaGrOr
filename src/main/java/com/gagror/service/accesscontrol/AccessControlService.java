package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.RegisterInput;

@Service
@Transactional
@CommonsLog
public class AccessControlService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	RequestAccountComponent requestAccount;

	PasswordEncoder passwordEncoder = GagrorAuthenticationConfiguration.getPasswordEncoder();

	public AccountEntity loadAccountEntity(final Authentication authentication) {
		if(null != authentication
				&& authentication.isAuthenticated()
				&& ! (authentication instanceof AnonymousAuthenticationToken)) {
			log.debug(String.format("Loading request account '%s'", authentication.getName()));
			return accountRepository.findByUsername(authentication.getName());
		} else {
			return null;
		}
	}

	public AccountEntity getRequestAccountEntity() {
		if(! requestAccount.isLoaded()) {
			final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			requestAccount.setAccount(loadAccountEntity(authentication));
			requestAccount.setLoaded(true);
		}
		return requestAccount.getAccount();
	}

	public AccountReferenceOutput getRequestAccount() {
		final AccountEntity account = getRequestAccountEntity();
		if(null != account) {
			log.debug(String.format("Loaded request account '%s' for output", account.getUsername()));
			return new AccountReferenceOutput(account);
		} else {
			log.debug("Cannot load request account for output, user not logged in");
			return null;
		}
	}

	public void register(final RegisterInput registerForm, final BindingResult bindingResult) {
		// Verify that the account can be created
		if(null != accountRepository.findByUsername(registerForm.getUsername())) {
			log.error(String.format("Attempt to create user '%s' failed, username busy", registerForm.getUsername()));
			registerForm.addErrorUsernameBusy(bindingResult);
		} else if(! registerForm.getPassword().equals(registerForm.getPasswordRepeat())) {
			log.error(String.format("Attempt to create user '%s' failed, password repeat mismatch", registerForm.getUsername()));
			registerForm.addErrorPasswordMismatch(bindingResult);
		} else {
			log.info(String.format("Registering user '%s'", registerForm.getUsername()));
			// Create the account
			final AccountEntity account = new AccountEntity(
					registerForm.getUsername(),
					passwordEncoder.encode(registerForm.getPassword()));
			accountRepository.save(account);
			// Automatically log in the newly registered user
			SecurityContextHolder.getContext().setAuthentication(
					new PreAuthenticatedAuthenticationToken(
							account.getUsername(),
							account.getPassword(),
							account.getAccountType().getAuthorities()));
		}
	}
}

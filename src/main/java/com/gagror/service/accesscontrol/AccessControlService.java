package com.gagror.service.accesscontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.RegisterInput;

@Service
@Transactional
public class AccessControlService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	RequestAccountComponent requestAccount;

	PasswordEncoder passwordEncoder = GagrorAuthenticationConfiguration.getPasswordEncoder();

	public AccountEntity getRequestAccountEntity() {
		if(! requestAccount.isLoaded()) {
			final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(null != authentication) {
				requestAccount.setAccount(accountRepository.findByUsername(authentication.getName()));
			}
			requestAccount.setLoaded(true);
		}
		return requestAccount.getAccount();
	}

	public AccountReferenceOutput getRequestAccount() {
		final AccountEntity account = getRequestAccountEntity();
		if(null != account) {
			return new AccountReferenceOutput(account);
		} else {
			return null;
		}
	}

	public void register(final RegisterInput registerForm, final BindingResult bindingResult) {
		// Verify that the account can be created
		if(null != accountRepository.findByUsername(registerForm.getUsername())) {
			registerForm.addErrorUsernameBusy(bindingResult);
		} else if(! registerForm.getPassword().equals(registerForm.getPasswordRepeat())) {
			registerForm.addErrorPasswordMismatch(bindingResult);
		} else {
			// Create the account
			accountRepository.save(new AccountEntity(
					registerForm.getUsername(),
					passwordEncoder.encode(registerForm.getPassword())));
			// TODO Automatically log in the newly registered user
		}
	}
}

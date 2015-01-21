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

import com.gagror.CodingErrorException;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountReferenceOutput;
import com.gagror.data.account.AccountRepository;

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
			log.trace(String.format("Loading request account '%s'", authentication.getName()));
			return accountRepository.findByName(authentication.getName());
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
			log.trace(String.format("Loaded request account '%s' for output", account.getName()));
			return new AccountReferenceOutput(account);
		} else {
			log.trace("Cannot load request account for output, user not logged in");
			return null;
		}
	}

	public boolean isPasswordTooWeak(final String rawPassword) {
		return rawPassword.length() < 5;
	}

	public String encodePassword(final String rawPassword) {
		if(isPasswordTooWeak(rawPassword)) {
			throw new CodingErrorException("Password was too weak, refusing to encode (it should have been verified)");
		}
		return passwordEncoder.encode(rawPassword);
	}

	public void logInAs(final AccountEntity account) {
		SecurityContextHolder.getContext().setAuthentication(
				new PreAuthenticatedAuthenticationToken(
						account.getName(),
						account.getPassword(),
						account.getAccountType().getAuthorities()));
	}
}

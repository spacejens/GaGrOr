package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.SecurityUser;

@Service
@CommonsLog
public class GagrorUserDetailsService implements UserDetailsService {

	@Autowired
	AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final AccountEntity account = accountRepository.findByUsername(username);
		if(null == account) {
			log.error(String.format("Failed to load security user '%s', account not found", username));
			throw new UsernameNotFoundException(String.format("Security user %s not found", username));
		}
		log.debug(String.format("Loaded security user '%s'", username));
		return new SecurityUser(account);
	}
}

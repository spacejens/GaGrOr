package com.gagror.service.accesscontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gagror.data.account.AccountEntity;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.account.SecurityUser;

@Service
public class GagrorUserDetailsService implements UserDetailsService {

	@Autowired
	AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final AccountEntity account = accountRepository.findByUsername(username);
		if(null == account) {
			throw new UsernameNotFoundException(String.format("User %s not found", username));
		}
		return new SecurityUser(account);
	}
}

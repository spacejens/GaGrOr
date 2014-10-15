package com.gagror.service.accesscontrol;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@CommonsLog
public class GagrorAuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	GagrorUserDetailsService userDetailsService;

	@Getter
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public void init(final AuthenticationManagerBuilder auth) throws Exception {
		log.info("Initializing GaGrOr authentication configuration");
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
	}
}

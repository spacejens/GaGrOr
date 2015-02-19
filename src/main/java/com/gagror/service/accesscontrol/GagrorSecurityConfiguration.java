package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * @see http://docs.spring.io/spring-security/site/docs/3.2.x/reference/htmlsingle/
 */
@Configuration
@EnableWebMvcSecurity
@CommonsLog
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GagrorSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		log.info("Configuring GaGrOr application security");
		// Authorize requests for various public resources
		http
			.authorizeRequests()
				.antMatchers("/webjars/**").permitAll();
		// After logging in, return to the page we logged in from
		final SavedRequestAwareAuthenticationSuccessHandler successHandler =
				new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setUseReferer(true);
		// Configuration for login process pages
		http
			.formLogin()
				.loginPage("/access/login")
				.permitAll()
				.successHandler(successHandler)
				.and()
			.logout()
				.logoutUrl("/access/logout")
				.permitAll();
	}
}

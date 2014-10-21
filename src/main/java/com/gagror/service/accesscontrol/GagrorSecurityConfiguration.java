package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.DefaultWebInvocationPrivilegeEvaluator;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @see http://docs.spring.io/spring-security/site/docs/3.2.x/reference/htmlsingle/
 */
@Configuration
@EnableWebMvcSecurity
@CommonsLog
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) // TODO Does this need to be here?
public class GagrorSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		log.info("Configuring GaGrOr application HTTP security");
		// Authorize requests for various public resources
		http
			.authorizeRequests()
				.antMatchers("/webjars/**").permitAll();
		// Configuration for login process pages
		http
			.formLogin()
				.loginPage("/access/login")
				.permitAll()
				.and()
			.logout()
				.logoutUrl("/access/logout")
				.permitAll();
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		log.info("Configuring GaGrOr application web security");
		final HttpSecurity http = getHttp();
		web.postBuildAction(new Runnable() {
			// TODO Does this need to be here?
			@Override
			public void run() {
				web.securityInterceptor(http.getSharedObject(FilterSecurityInterceptor.class));
				web.privilegeEvaluator(new InterceptingPrivilegeEvaluator(http.getSharedObject(FilterSecurityInterceptor.class)));
			}
		});
	}

	private class InterceptingPrivilegeEvaluator extends DefaultWebInvocationPrivilegeEvaluator {

		public InterceptingPrivilegeEvaluator(final AbstractSecurityInterceptor securityInterceptor) {
			super(securityInterceptor);
		}

		@Override
		public boolean isAllowed(final String uri, final Authentication authentication) {
			log.info(String.format("Checking if URI %s is allowed for %s", uri, authentication));
			return super.isAllowed(uri, authentication);
		}

		@Override
		public boolean isAllowed(final String contextPath, final String uri, final String method, final Authentication authentication) {
			log.info(String.format("Checking if context %s, method %s, URI %s is allowed for %s", contextPath, method, uri, authentication));
			return super.isAllowed(contextPath, uri, method, authentication);
		}
	}
}

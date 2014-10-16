package com.gagror.service.accesscontrol;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
@CommonsLog
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
//TODO http://spring.io/guides/gs/securing-web/
//TODO http://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html
//TODO http://www.sivalabs.in/2014/03/springmvc4-spring-data-jpa.html
public class GagrorSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		log.info("Configuring GaGrOr application security");
		// Authorize requests for various public resources and URLs
		http
			.authorizeRequests()
				.antMatchers("/", "/webjars/**").permitAll()
				.antMatchers("/access/register").not().authenticated()
				.anyRequest().authenticated();
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
}

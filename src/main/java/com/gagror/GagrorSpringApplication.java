package com.gagror;

import java.util.Arrays;

import javax.servlet.Filter;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import com.gagror.service.accesscontrol.GagrorPermissionEvaluator;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@CommonsLog
public class GagrorSpringApplication extends SpringBootServletInitializer {

	/**
	 * This method is not used when deploying WAR to server, but it is used
	 * when running the project as an application in Eclipse (or another IDE).
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		log.info("Starting GaGrOr as a standalone application");
		ApplicationContext ctx = SpringApplication.run(GagrorSpringApplication.class, args);

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			log.trace("Found Spring bean: " + beanName);
		}
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		log.info("Configuring GaGrOr application in a container");
		return application.sources(GagrorSpringApplication.class);
	}

	@Bean
	public Filter requestLoggingFilter() {
		log.info("Adding request logging filter");
		return new CommonsRequestLoggingFilter();
	}

	@Bean
	public PermissionEvaluator permissionEvaluator() {
		log.info("Adding permission evaluator");
		return new GagrorPermissionEvaluator();
	}
}

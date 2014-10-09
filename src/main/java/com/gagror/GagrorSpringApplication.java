package com.gagror;

import java.util.Arrays;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
			log.debug("Found Spring bean: " + beanName);
		}
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		log.info("Configuring GaGrOr application in a container");
		return application.sources(GagrorSpringApplication.class);
	}
}

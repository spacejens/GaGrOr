package com.gagror;

import java.util.Arrays;

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
public class GagrorSpringApplication extends SpringBootServletInitializer {

	/**
	 * This method is not used when deploying WAR to server, but it is used
	 * when running the project as an application in Eclipse (or another IDE).
	 * For this reason, it produces some debug outputs directly to standard out.
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		ApplicationContext ctx = SpringApplication.run(GagrorSpringApplication.class, args);

		System.out.println("Spring beans:");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println("\t" + beanName);
		}
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(GagrorSpringApplication.class);
	}
}

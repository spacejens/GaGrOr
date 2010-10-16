package se.spacejens.gagror;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 * 
 */
public class App {
	private static Logger log = LoggerFactory.getLogger(App.class);

	public static void main(final String[] args) {
		App.log.debug("Creating instance");
		final App app = new App();
		System.out.println(app.getMessage());
		App.log.debug("Exiting...");
	}

	public String getMessage() {
		App.log.info("Getting message");
		App.log.debug("Additional details about message generation");
		return "Hello World!";
	}
}

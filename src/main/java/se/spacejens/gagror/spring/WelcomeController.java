package se.spacejens.gagror.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Controller for the welcome page.
 * 
 * @author spacejens
 */
public class WelcomeController implements Controller {

	private static final Logger log = LoggerFactory
			.getLogger(WelcomeController.class);

	@Override
	public ModelAndView handleRequest(final HttpServletRequest arg0,
			final HttpServletResponse arg1) {
		WelcomeController.log.info("Handling request");
		return new ModelAndView("welcome");
	}
}

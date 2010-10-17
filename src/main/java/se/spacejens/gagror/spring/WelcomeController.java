package se.spacejens.gagror.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Controller for the welcome page.
 * 
 * @author spacejens
 */
public class WelcomeController implements Controller {

	@Override
	public ModelAndView handleRequest(final HttpServletRequest arg0,
			final HttpServletResponse arg1) throws Exception {
		return new ModelAndView("welcome");
	}
}

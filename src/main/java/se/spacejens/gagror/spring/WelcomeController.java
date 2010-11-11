package se.spacejens.gagror.spring;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import se.spacejens.gagror.LogAwareSupport;

/**
 * Controller for the welcome page.
 * 
 * @author spacejens
 */
public class WelcomeController extends LogAwareSupport implements Controller {

	@Override
	public ModelAndView handleRequest(final HttpServletRequest arg0,
			final HttpServletResponse arg1) {
		this.getLog().info("Handling request and providing model");
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("brief", "A wild message appears!");
		return new ModelAndView("welcome", "gagror", model);
	}
}

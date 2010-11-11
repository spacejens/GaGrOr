package se.spacejens.gagror.spring;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import se.spacejens.gagror.LogAwareSupport;

/**
 * Controller for the welcome page.
 * 
 * @author spacejens
 */
@Controller
@RequestMapping("/welcome")
public class WelcomeController extends LogAwareSupport {

	@RequestMapping("/initial.html")
	public ModelAndView handleRequest(final HttpServletRequest arg0,
			final HttpServletResponse arg1) {
		this.getLog().info("Handling request and providing model");
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("brief", "A wild message appears!");
		return new ModelAndView("welcome", "gagror", model);
	}
}

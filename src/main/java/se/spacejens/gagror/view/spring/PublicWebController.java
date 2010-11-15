package se.spacejens.gagror.view.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for all public web pages (i.e. pages not requiring the user to be
 * logged in).
 * 
 * @author spacejens
 */
@Controller
@RequestMapping("/")
public class PublicWebController extends ControllerSupport {

	/**
	 * The index page is the first page seen by visitors. It provides
	 * functionality to log in and to register new users. Finally, it also
	 * displays some general information about the system.
	 * 
	 * @return View to the index page.
	 */
	@RequestMapping("/index.html")
	public ModelAndView index() {
		return new ModelAndView("index");
	}
}

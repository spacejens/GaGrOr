package se.spacejens.gagror.view.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import se.spacejens.gagror.GagrorException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.model.user.User;

/**
 * Spring controller for all public web pages (i.e. pages not requiring the user
 * to be logged in).
 * 
 * @author spacejens
 */
@Controller
@RequestMapping("/")
public class PublicPages extends SpringViewSupport {

	/**
	 * The index page is the first page seen by visitors. It provides
	 * functionality to log in and a link to register new users. Finally, it
	 * also displays some general information about the system.
	 * 
	 * @return View to the index page.
	 */
	@RequestMapping("/index.html")
	public ModelAndView index() {
		this.getLog().debug("Serving index page");
		return new ModelAndView("index");
	}

	/**
	 * This page provides a form used for registering a new user.
	 * 
	 * @return View to the registration form.
	 */
	@RequestMapping(value = "/register.html", method = RequestMethod.GET)
	public ModelAndView registerFormGet() {
		this.getLog().debug("Serving registration page");
		final ModelAndView mav = new ModelAndView("register");
		mav.getModel().put("registrationform", new UserRegistrationForm());
		return mav;
	}

	/**
	 * Posting user registration form. If registration is successful, the user
	 * will be automatically logged in.
	 * 
	 * @param registrationform
	 *            The posted form contents.
	 * @param result
	 *            Result of data binding.
	 * @return View to registration form (with error messages) if failed or
	 *         cancelled, redirect to system dashboard if successful.
	 */
	@RequestMapping(value = "/register.html", method = RequestMethod.POST)
	public ModelAndView registerFormPost(final HttpServletRequest request, @ModelAttribute final UserRegistrationForm registrationform) {
		this.getLog().debug("Registration form posted");
		final RequestContext rc = this.getContext(request);
		final ModelAndView mav = new ModelAndView();
		try {
			// TODO Check for binding errors
			final User user = this.getLoginService().registerUser(rc, registrationform.getUsername(), registrationform.getPassword());
			// TODO Log in as the newly registered user
			mav.setView(new RedirectView("/index.html"));
		} catch (GagrorException e) {
			this.getLog().error("Exception while registering user", e);
			// TODO Add error messages
			mav.setViewName("register");
			mav.getModel().put("registrationform", registrationform);
			return mav;
		}
		return mav;
	}
}

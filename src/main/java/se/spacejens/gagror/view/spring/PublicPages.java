package se.spacejens.gagror.view.spring;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.MayNotBeLoggedInException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.controller.helper.RepeatedPasswordNotMatchingException;
import se.spacejens.gagror.model.user.User;
import se.spacejens.gagror.model.user.UserCreationException;

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
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public ModelAndView index() {
		this.getLog().debug("Serving index page");
		final ModelAndView mav = new ModelAndView("index");
		mav.getModel().put("loginForm", new LoginForm());
		return mav;
	}

	/**
	 * Posting login form on index page.
	 * 
	 * @param request
	 *            HTTP request.
	 * @param loginForm
	 *            The posted form contents.
	 * @param result
	 *            Result of data binding.
	 * @return View to login failed page (with error messages) if failed,
	 *         redirect to system dashboard if successful.
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.POST)
	public ModelAndView postLoginFormIndex(final HttpServletRequest request, @Valid final LoginForm loginForm, final BindingResult result) {
		this.getLog().debug("Login form posted from index page");
		return this.postLoginForm(request, loginForm, result);
	}

	/**
	 * This page is displayed when the user is logged out, either by choice or
	 * by force.
	 * 
	 * @return View to the logged out page.
	 */
	@RequestMapping(value = "/loggedout.html", method = RequestMethod.GET)
	public ModelAndView loggedOut() {
		this.getLog().debug("Serving logged out page");
		final ModelAndView mav = new ModelAndView("loggedout");
		mav.getModel().put("loginForm", new LoginForm());
		return mav;
	}

	/**
	 * Posting login form on logged out page.
	 * 
	 * @param request
	 *            HTTP request.
	 * @param loginForm
	 *            The posted form contents.
	 * @param result
	 *            Result of data binding.
	 * @return View to login failed page (with error messages) if failed,
	 *         redirect to system dashboard if successful.
	 */
	@RequestMapping(value = "/loggedout.html", method = RequestMethod.POST)
	public ModelAndView postLoginFormLoggedOut(final HttpServletRequest request, @Valid final LoginForm loginForm, final BindingResult result) {
		this.getLog().debug("Login form posted from logged out page");
		return this.postLoginForm(request, loginForm, result);
	}

	/**
	 * Page shown when login fails
	 * 
	 * @return View to the login failed page.
	 */
	@RequestMapping(value = "/loginfailed.html", method = RequestMethod.GET)
	public ModelAndView loginFailedGet() {
		this.getLog().debug("Serving login failed page");
		final ModelAndView mav = new ModelAndView("loginfailed");
		mav.getModel().put("loginForm", new LoginForm());
		return mav;
	}

	/**
	 * Posting login form on login failed page.
	 * 
	 * @param request
	 *            HTTP request.
	 * @param loginForm
	 *            The posted form contents.
	 * @param result
	 *            Result of data binding.
	 * @return View to login failed page (with error messages) if failed,
	 *         redirect to system dashboard if successful.
	 */
	@RequestMapping(value = "/loginfailed.html", method = RequestMethod.POST)
	public ModelAndView loginFailedPost(final HttpServletRequest request, @Valid final LoginForm loginForm, final BindingResult result) {
		this.getLog().debug("Login form posted from login failed page");
		return this.postLoginForm(request, loginForm, result);
	}

	/**
	 * Handle posting of login form.
	 * 
	 * @param request
	 *            HTTP request.
	 * @param loginForm
	 *            The posted form contents.
	 * @param result
	 *            Result of data binding.
	 * @return View to login failed page (with error messages) if failed,
	 *         redirect to system dashboard if successful.
	 * @throws LoginFailedException
	 * @throws ServiceCommunicationException
	 */
	private ModelAndView postLoginForm(final HttpServletRequest request, @Valid final LoginForm loginForm, final BindingResult result) {
		final RequestContext rc = this.getContext(request);
		final ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			this.getLog().debug("Login form has binding errors");
			mav.setViewName("loginfailed");
			// Framework adds registration form and binding result automatically
			return mav;
		}
		try {
			final User user = this.getLoginService().loginUser(rc, loginForm.getUsername(), loginForm.getPassword());
			this.setLoggedInUser(user, request.getSession());
			mav.setView(new RedirectView(rc.getContextPath() + "/dashboard/index.html"));
			return mav;
		} catch (final LoginFailedException e) {
			this.getLog().debug("Login failed for user {}", loginForm.getUsername());
			final ObjectError error = new ObjectError("loginForm", "wrong username/password combination");
			result.addError(error);
			mav.setViewName("loginfailed");
			// Framework adds registration form and binding result automatically
			return mav;
		} catch (ServiceCommunicationException e) {
			this.getLog().debug("Failure to communicate with service when registering user");
			final ObjectError error = new ObjectError("loginForm", "communication problem");
			result.addError(error);
			mav.setViewName("register");
			// Framework adds registration form and binding result automatically
			return mav;
		}
	}

	/**
	 * This page provides a form used for registering a new user.
	 * 
	 * @return View to the registration form.
	 */
	@RequestMapping(value = "/register.html", method = RequestMethod.GET)
	public ModelAndView registerFormGet(final HttpServletRequest request) {
		this.getLog().debug("Serving registration page");
		// TODO Verify that the user is not logged in
		final RequestContext rc = this.getContext(request);
		final ModelAndView mav = new ModelAndView("register");
		mav.getModel().put("userRegistrationForm", new UserRegistrationForm());
		return mav;
	}

	/**
	 * Posting user registration form. If registration is successful, the user
	 * will be automatically logged in.
	 * 
	 * @param request
	 *            HTTP request.
	 * @param userRegistrationForm
	 *            The posted form contents.
	 * @param result
	 *            Result of data binding.
	 * @return View to registration form (with error messages) if failed,
	 *         redirect to system dashboard if successful.
	 */
	@RequestMapping(value = "/register.html", method = RequestMethod.POST)
	public ModelAndView registerFormPost(final HttpServletRequest request, @Valid final UserRegistrationForm userRegistrationForm,
			final BindingResult result) {
		this.getLog().debug("Registration form posted");
		final ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			this.getLog().debug("Registration form has binding errors");
			mav.setViewName("register");
			// Framework adds registration form and binding result automatically
			return mav;
		}
		final RequestContext rc = this.getContext(request);
		try {
			final User user = this.getLoginService().registerUser(rc, userRegistrationForm.getUsername(), userRegistrationForm.getPassword(),
					userRegistrationForm.getRepeatPassword());
			this.setLoggedInUser(user, request.getSession());
			mav.setView(new RedirectView(rc.getContextPath() + "/index.html"));
			return mav;
		} catch (RepeatedPasswordNotMatchingException e) {
			this.getLog().debug("Repeated password did not match when registering user");
			final FieldError error = new FieldError("userRegistrationForm", "repeatPassword", "passwords did not match");
			result.addError(error);
			mav.setViewName("register");
			// Framework adds registration form and binding result automatically
			return mav;
		} catch (final UserCreationException e) {
			this.getLog().debug("User could not be registered, assume that the username is not unique");
			final FieldError error = new FieldError("userRegistrationForm", "username", "username is not unique");
			result.addError(error);
			mav.setViewName("register");
			// Framework adds registration form and binding result automatically
			return mav;
		} catch (final ServiceCommunicationException e) {
			this.getLog().debug("Failure to communicate with service when registering user");
			final ObjectError error = new ObjectError("userRegistrationForm", "communication problem");
			result.addError(error);
			mav.setViewName("register");
			// Framework adds registration form and binding result automatically
			return mav;
		} catch (MayNotBeLoggedInException e) {
			this.getLog().debug("Attempting to register a new user while logged in");
			// TODO Go to a more suitable page
			mav.setView(new RedirectView(rc.getContextPath() + "/index.html"));
			return mav;
		}
	}
}

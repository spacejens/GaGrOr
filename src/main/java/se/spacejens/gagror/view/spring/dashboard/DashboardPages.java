package se.spacejens.gagror.view.spring.dashboard;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.NotLoggedInException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.view.spring.SpringViewSupport;

/**
 * Spring controller for the dashboard, the central part of the system and the
 * first destination when a user logs in.
 * 
 * @author spacejens
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardPages extends SpringViewSupport {

	/**
	 * The dashboard index page is the first page the user arrives at when
	 * logging in.
	 * 
	 * @param request
	 *            HTTP request.
	 * @return View to dashboard page if logged in, view to logged out page
	 *         otherwise.
	 */
	@RequestMapping("/index.html")
	public ModelAndView index(final HttpServletRequest request) {
		this.getLog().debug("Serving dashboard index");
		final RequestContext rc = this.getContext(request);
		final ModelAndView mav = new ModelAndView();
		try {
			this.getLoginService().verifyLogin(rc);
			mav.setViewName("dashboard/index");
		} catch (LoginFailedException e) {
			this.getLog().debug("Login failed on dashboard index, redirecting to logged out page");
			mav.setView(new RedirectView(rc.getContextPath() + "/loggedout.html"));
		} catch (ServiceCommunicationException e) {
			this.getLog().debug("Service communication problems on dashboard index, redirecting to logged out page");
			mav.setView(new RedirectView(rc.getContextPath() + "/loggedout.html"));
		} catch (NotLoggedInException e) {
			this.getLog().debug("Not logged in on dashboard index, redirecting to logged out page");
			mav.setView(new RedirectView(rc.getContextPath() + "/loggedout.html"));
		}
		return mav;
	}
}

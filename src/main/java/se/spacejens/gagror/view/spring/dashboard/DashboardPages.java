package se.spacejens.gagror.view.spring.dashboard;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.NotLoggedInException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.view.Views;
import se.spacejens.gagror.view.spring.SpringRequestMappings;
import se.spacejens.gagror.view.spring.SpringViewSupport;

/**
 * Spring controller for the dashboard, the central part of the system and the
 * first destination when a user logs in.
 * 
 * @author spacejens
 */
@Controller
@RequestMapping(SpringRequestMappings.DASHBOARD)
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
	@RequestMapping(SpringRequestMappings.DASHBOARD_INDEX)
	public ModelAndView index(final HttpServletRequest request) {
		return new WorkLoggedIn() {
			@Override
			protected ModelAndView doWorkLoggedIn(final RequestContext rc) throws NotLoggedInException, LoginFailedException,
					ServiceCommunicationException {
				this.getLog().debug("Serving dashboard index");
				final ModelAndView mav = new ModelAndView(Views.DASHBOARD_INDEX.getName());
				// TODO Replace with service call to get info, add to model
				DashboardPages.this.getLoginService().verifyLogin(rc);
				return mav;
			}
		}.process(request);
	}
}

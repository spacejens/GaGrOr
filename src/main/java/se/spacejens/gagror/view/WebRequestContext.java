package se.spacejens.gagror.view;

import javax.servlet.http.HttpServletRequest;

import se.spacejens.gagror.LogAwareSupport;
import se.spacejens.gagror.controller.RequestContext;

/**
 * Request context for controller layer requests originating from the view
 * layer.
 * 
 * @author spacejens
 */
class WebRequestContext extends LogAwareSupport implements RequestContext {

	/**
	 * Create instance from information stored in servlet request.
	 * 
	 * @param request
	 *            Not null.
	 */
	public WebRequestContext(final HttpServletRequest request) {
		this.getLog().debug(
				"Created web request context from HTTP servlet request");
	}
}

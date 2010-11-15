package se.spacejens.gagror.view;

import javax.servlet.http.HttpServletRequest;

import se.spacejens.gagror.LogAwareSupport;
import se.spacejens.gagror.controller.RequestContext;

/**
 * Superclass for all views, providing shared functionality.
 * 
 * @author spacejens
 */
public abstract class ViewSupport extends LogAwareSupport {

	/**
	 * Extract request context information from a servlet request.
	 * 
	 * @param request
	 *            Not null.
	 * @return Not null.
	 */
	protected RequestContext getContext(final HttpServletRequest request) {
		return new WebRequestContext(request);
	}
}

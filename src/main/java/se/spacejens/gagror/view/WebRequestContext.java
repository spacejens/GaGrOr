package se.spacejens.gagror.view;

import javax.servlet.http.HttpServletRequest;

import se.spacejens.gagror.controller.RequestContextSupport;

/**
 * Request context for controller layer requests originating from the view
 * layer.
 * 
 * @author spacejens
 */
public class WebRequestContext extends RequestContextSupport {

	/**
	 * Create instance from information stored in servlet request.
	 * 
	 * @param request
	 *            Not null.
	 */
	public WebRequestContext(final HttpServletRequest request) {
	}
}

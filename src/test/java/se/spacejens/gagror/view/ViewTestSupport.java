package se.spacejens.gagror.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mockito.Mockito;

import se.spacejens.gagror.TestSupport;

public abstract class ViewTestSupport extends TestSupport {

	/**
	 * Create a mocked request, containing the specified data.
	 * 
	 * @param username
	 *            Username used for login in this session.
	 * @param password
	 *            Encrypted password used for login in this session.
	 * @return The mocked request.
	 */
	protected HttpServletRequest createMockRequest(final String username, final String password) {
		final HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(session.getAttribute("username")).thenReturn(username);
		Mockito.when(session.getAttribute("password")).thenReturn(password);
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getSession()).thenReturn(session);
		return request;
	}
}

package se.spacejens.gagror.view.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit test for {@link Welcome}.
 * 
 * @author spacejens
 */
public class WelcomeTest extends SpringViewTestSupport<Welcome> {

	/**
	 * Verify that the correct model and view are returned.
	 */
	@Test
	public void testModelAndView() {
		this.log.info("Testing WelcomeController");
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		final ModelAndView result = this.getInstance().handleRequest(request, response);
		Mockito.verifyZeroInteractions(request);
		Assert.assertEquals("Unexpected view name", "welcome", result.getViewName());
		Assert.assertNotNull("No model received", result.getModel());
		Assert.assertNotNull("Model did not contain brief message", result.getModel().get("brief"));
	}

	@Override
	protected Welcome createInstance() {
		return new Welcome();
	}
}

package se.spacejens.gagror.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import se.spacejens.gagror.AbstractTest;

/**
 * Unit test for {@link WelcomeController}.
 * 
 * @author spacejens
 */
public class WelcomeControllerTest extends AbstractTest {

	/** Instance to test. */
	private WelcomeController instance;

	/**
	 * Each test method receives a fresh instance to test.
	 */
	@Before
	public void setup() {
		this.instance = new WelcomeController();
	}

	/**
	 * Verify that the correct model and view are returned.
	 */
	@Test
	public void testModelAndView() {
		this.log.info("Testing WelcomeController");
		final HttpServletRequest request = Mockito
				.mock(HttpServletRequest.class);
		final HttpServletResponse response = Mockito
				.mock(HttpServletResponse.class);
		final ModelAndView result = this.instance.handleRequest(request,
				response);
		Mockito.verifyZeroInteractions(request);
		Assert.assertEquals("Unexpected view name", "welcome",
				result.getViewName());
		Assert.assertEquals("No model expected for this page", 0, result
				.getModel().size());
	}
}

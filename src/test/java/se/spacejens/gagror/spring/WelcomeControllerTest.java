package se.spacejens.gagror.spring;

import java.util.Map;

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
		Assert.assertNotNull("No model received", result.getModel());
		Assert.assertNotNull("Gagror model not received", result.getModel()
				.get("gagror"));
		if (!(result.getModel().get("gagror") instanceof Map)) {
			Assert.fail("Unexpected type of gagror model: "
					+ result.getModel().get("gagror").getClass());
		}
		@SuppressWarnings("unchecked")
		final Map<String, Object> model = (Map<String, Object>) result
				.getModel().get("gagror");
		Assert.assertNotNull("Model did not contain brief message",
				model.get("brief"));
	}
}

package se.spacejens.gagror.view.spring;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit test for {@link WelcomeController}.
 * 
 * @author spacejens
 */
public class WelcomeControllerTest extends
		ControllerTestSupport<WelcomeController> {

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
		final ModelAndView result = this.getInstance().handleRequest(request,
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

	@Override
	protected WelcomeController createInstance() {
		return new WelcomeController();
	}
}

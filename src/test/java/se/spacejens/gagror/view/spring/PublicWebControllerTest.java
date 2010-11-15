package se.spacejens.gagror.view.spring;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit test for {@link PublicWebController}.
 * 
 * @author spacejens
 */
public class PublicWebControllerTest extends
		ControllerTestSupport<PublicWebController> {

	/**
	 * Test of {@link PublicWebController#index()}.
	 * 
	 * @throws Exception
	 *             If unexpected errors occur.
	 */
	@Test
	public void testIndex() throws Exception {
		final ModelAndView result = this.getInstance().index();
		Assert.assertEquals("Unexpected view name", "index",
				result.getViewName());
	}

	@Override
	protected PublicWebController createInstance() {
		return new PublicWebController();
	}
}

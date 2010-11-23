package se.spacejens.gagror.view.spring;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit test for {@link PublicPages}.
 * 
 * @author spacejens
 */
public class PublicPagesTest extends SpringViewTestSupport<PublicPages> {

	/**
	 * Test of {@link PublicPages#index()}.
	 * 
	 * @throws Exception
	 *             If unexpected errors occur.
	 */
	@Test
	public void testIndex() throws Exception {
		final HttpServletRequest request = this.createMockRequest(null, null);
		final ModelAndView result = this.getInstance().index(request);
		Assert.assertEquals("Unexpected view name", "index", result.getViewName());
	}

	@Override
	protected PublicPages createInstance() {
		return new PublicPages();
	}
}

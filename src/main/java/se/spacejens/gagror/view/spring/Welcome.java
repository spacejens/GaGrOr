package se.spacejens.gagror.view.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.model.Message;

/**
 * Spring controller for the welcome page.
 * 
 * @author spacejens
 */
@Controller
@RequestMapping("/welcome")
public class Welcome extends SpringViewSupport {

	@RequestMapping("/initial.html")
	public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) {
		this.getLog().info("Handling request and providing model");
		final ModelAndView mav = new ModelAndView();
		try {
			final Message message = this.getMessageService().createMessage(this.getContext(request), "An EJB generated persistent message appears!");
			mav.getModel().put("brief", message.getText() + " (" + message.getId() + ")");
		} catch (final ServiceCommunicationException e) {
			mav.getModel().put("brief", "An error message appears: " + e.getCause().getMessage());
		} catch (final LoginFailedException e) {
			mav.getModel().put("brief", "A login failed message appears");
		}
		mav.setViewName("welcome");
		return mav;
	}
}

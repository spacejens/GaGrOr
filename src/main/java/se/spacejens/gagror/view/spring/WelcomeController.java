package se.spacejens.gagror.view.spring;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import se.spacejens.gagror.model.Message;

/**
 * Controller for the welcome page.
 * 
 * @author spacejens
 */
@Controller
@RequestMapping("/welcome")
public class WelcomeController extends ControllerSupport {

	@RequestMapping("/initial.html")
	public ModelAndView handleRequest(final HttpServletRequest request,
			final HttpServletResponse response) {
		this.getLog().info("Handling request and providing model");
		final Map<String, Object> model = new HashMap<String, Object>();
		final EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GagrorPersistenceUnit");
		final EntityManager em = emf.createEntityManager();
		final EntityTransaction trans = em.getTransaction();
		trans.begin();
		Message mess1 = new Message("A persistent message appears!");
		em.persist(mess1);
		model.put("brief", mess1.getText() + " (" + mess1.getId() + ")");
		trans.commit();
		em.close();
		emf.close();
		return new ModelAndView("welcome", "gagror", model);
	}
}

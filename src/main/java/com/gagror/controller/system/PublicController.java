package com.gagror.controller.system;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gagror.controller.AbstractController;

@Controller
@CommonsLog
public class PublicController extends AbstractController {

	@PreAuthorize(IS_PUBLIC)
	@RequestMapping("/")
	public String about(final Model model) {
		log.info("Viewing about page");
		return "about";
	}
}

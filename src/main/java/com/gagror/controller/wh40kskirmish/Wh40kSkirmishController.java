package com.gagror.controller.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gagror.service.wh40kskirmish.Wh40kSkirmishService;

@Controller
@RequestMapping("/wh40kskirmish/group")
@CommonsLog
public class Wh40kSkirmishController extends AbstractWh40kSkirmishController {

	@Autowired
	Wh40kSkirmishService wh40kSkirmishService;

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}")
	public String viewGroup(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing group %d", groupId));
		model.addAttribute("group", wh40kSkirmishService.viewGroup(groupId));
		return "wh40kskirmish/view_group";
	}
}

package com.gagror.controller.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gagror.controller.AbstractController;
import com.gagror.service.wh40kskirmish.gangs.Wh40kSkirmishGangService;

@Controller
@RequestMapping("/wh40kskirmish/gang")
@CommonsLog
public class Wh40kSkirmishGangController extends AbstractController {

	protected static final String ATTR_GANGTYPE_ID = "gangTypeId";
	protected static final String ATTR_FACTION_ID = "factionId";
	protected static final String ATTR_GANG_ID = "gangId";

	@Autowired
	Wh40kSkirmishGangService gangService;

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_FACTION_ID + "}/{" + ATTR_GANG_ID + "}")
	public String viewGang(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_FACTION_ID) final Long factionId,
			@PathVariable(ATTR_GANG_ID) final Long gangId,
			final Model model) {
		log.info(String.format("Viewing gang %d of faction %d of gang type %d in group %d", gangId, factionId, gangTypeId, groupId));
		model.addAttribute("gang", gangService.viewGang(groupId, gangTypeId, factionId, gangId));
		// TODO Show fighters on view gang page
		return "wh40kskirmish/gang_view";
	}

	// TODO Add page to create gang (who is allowed?)

	// TODO Add page to edit gang (group owner unlocks additional capabilities? or can only group owner edit? separate pages?)

	// TODO Add page to create (buy) or edit fighter (group owner unlocks additional capabilities?)

	// TODO Add page to view fighter
}

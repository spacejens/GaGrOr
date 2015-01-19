package com.gagror.controller.wh40kskirmish;

import javax.validation.Valid;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gagror.controller.AbstractController;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFactionOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishFighterTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishGangTypeOutput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceInput;
import com.gagror.data.wh40kskirmish.rules.gangs.Wh40kSkirmishRaceOutput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryInput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryCategoryOutput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeInput;
import com.gagror.data.wh40kskirmish.rules.territory.Wh40kSkirmishTerritoryTypeOutput;
import com.gagror.service.social.GroupService;
import com.gagror.service.wh40kskirmish.Wh40kSkirmishFactionPersister;
import com.gagror.service.wh40kskirmish.Wh40kSkirmishFighterTypePersister;
import com.gagror.service.wh40kskirmish.Wh40kSkirmishGangTypePersister;
import com.gagror.service.wh40kskirmish.Wh40kSkirmishRacePersister;
import com.gagror.service.wh40kskirmish.Wh40kSkirmishRulesService;
import com.gagror.service.wh40kskirmish.Wh40kSkirmishTerritoryCategoryPersister;
import com.gagror.service.wh40kskirmish.Wh40kSkirmishTerritoryTypePersister;

@Controller
@RequestMapping("/wh40kskirmish/rules")
@CommonsLog
public class Wh40kSkirmishRulesController extends AbstractController {

	protected static final String ATTR_GANGTYPE_ID = "gangTypeId";
	protected static final String ATTR_FACTION_ID = "factionId";
	protected static final String ATTR_RACE_ID = "raceId";
	protected static final String ATTR_FIGHTERTYPE_ID = "fighterTypeId";
	protected static final String ATTR_TERRITORYCATEGORY_ID = "territoryCategoryId";
	protected static final String ATTR_TERRITORYTYPE_ID = "territoryTypeId";

	@Autowired
	GroupService groupService;

	@Autowired
	Wh40kSkirmishRulesService rulesService;

	@Autowired
	Wh40kSkirmishGangTypePersister gangTypePersister;

	@Autowired
	Wh40kSkirmishFactionPersister factionPersister;

	@Autowired
	Wh40kSkirmishRacePersister racePersister;

	@Autowired
	Wh40kSkirmishFighterTypePersister fighterTypePersister;

	@Autowired
	Wh40kSkirmishTerritoryCategoryPersister territoryCategoryPersister;

	@Autowired
	Wh40kSkirmishTerritoryTypePersister territoryTypePersister;

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}")
	public String viewRules(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing rules for group %d", groupId));
		model.addAttribute("rules", rulesService.viewRules(groupId));
		return "wh40kskirmish/rules_view";
	}

	// TODO Add page to edit basic rules

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/gangtype/create", method=RequestMethod.GET)
	public String createGangTypeForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create gang type form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("gangTypeForm", new Wh40kSkirmishGangTypeInput(groupId));
		return "wh40kskirmish/gangtypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/gangtype/save", method=RequestMethod.POST)
	public Object saveGangTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("gangTypeForm") final Wh40kSkirmishGangTypeInput gangTypeForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(gangTypeForm.getGroupId())) {
			log.error(String.format("Group ID URL (%d) and form (%d) mismatch when attempting to save gang type form", groupId, gangTypeForm.getGroupId()));
			throw new IllegalArgumentException(String.format("Unexpected group ID in gang type form"));
		}
		if(gangTypePersister.save(gangTypeForm, bindingResult)) {
			log.info(String.format("Saved gang type: %s", gangTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", gangTypeForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/gangtypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/gangtype/{" + ATTR_GANGTYPE_ID + "}")
	public String viewGangType(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			final Model model) {
		model.addAttribute("gangType", rulesService.viewGangType(groupId, gangTypeId));
		return "wh40kskirmish/gangtypes_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/gangtype/{" + ATTR_GANGTYPE_ID + "}/edit", method=RequestMethod.GET)
	public String editGangTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			final Model model) {
		log.info(String.format("Viewing edit gang type form for gang type %d in group %d", gangTypeId, groupId));
		final Wh40kSkirmishGangTypeOutput gangType = rulesService.viewGangType(groupId, gangTypeId);
		model.addAttribute("group", gangType.getGroup());
		model.addAttribute("gangTypeForm", new Wh40kSkirmishGangTypeInput(gangType));
		return "wh40kskirmish/gangtypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/faction/{" + ATTR_GANGTYPE_ID + "}/create", method=RequestMethod.GET)
	public String createFactionForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			final Model model) {
		log.info(String.format("Viewing create faction form for gang type %d of group %d", gangTypeId, groupId));
		model.addAttribute("gangType", rulesService.viewGangType(groupId, gangTypeId));
		model.addAttribute("factionForm", new Wh40kSkirmishFactionInput(groupId, gangTypeId));
		return "wh40kskirmish/factions_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/faction/save", method=RequestMethod.POST)
	public Object saveFactionForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("factionForm") final Wh40kSkirmishFactionInput factionForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(factionForm.getGroupId())) {
			log.error(String.format("Group ID URL (%d) and form (%d) mismatch when attempting to save faction form", groupId, factionForm.getGroupId()));
			throw new IllegalArgumentException(String.format("Unexpected group ID in faction form"));
		}
		if(factionPersister.save(factionForm, bindingResult)) {
			log.info(String.format("Saved faction: %s", factionForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", factionForm));
			model.addAttribute("gangType", rulesService.viewGangType(groupId, factionForm.getGangTypeId()));
			return "wh40kskirmish/factions_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/faction/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_FACTION_ID + "}")
	public String viewFaction(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_FACTION_ID) final Long factionId,
			final Model model) {
		model.addAttribute("faction", rulesService.viewFaction(groupId, gangTypeId, factionId));
		return "wh40kskirmish/factions_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/faction/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_FACTION_ID + "}/edit", method=RequestMethod.GET)
	public String editFactionForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_FACTION_ID) final Long factionId,
			final Model model) {
		log.info(String.format("Viewing edit faction form for faction %d of gang type %d in group %d", factionId, gangTypeId, groupId));
		final Wh40kSkirmishFactionOutput faction = rulesService.viewFaction(groupId, gangTypeId, factionId);
		model.addAttribute("gangType", faction.getGangType());
		model.addAttribute("factionForm", new Wh40kSkirmishFactionInput(faction));
		return "wh40kskirmish/factions_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/race/{" + ATTR_GANGTYPE_ID + "}/create", method=RequestMethod.GET)
	public String createRaceForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			final Model model) {
		log.info(String.format("Viewing create race form for gang type %d of group %d", gangTypeId, groupId));
		model.addAttribute("gangType", rulesService.viewGangType(groupId, gangTypeId));
		model.addAttribute("raceForm", new Wh40kSkirmishRaceInput(groupId, gangTypeId));
		return "wh40kskirmish/races_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/race/save", method=RequestMethod.POST)
	public Object saveRaceForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("raceForm") final Wh40kSkirmishRaceInput raceForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(raceForm.getGroupId())) {
			log.error(String.format("Group ID URL (%d) and form (%d) mismatch when attempting to save race form", groupId, raceForm.getGroupId()));
			throw new IllegalArgumentException(String.format("Unexpected group ID in race form"));
		}
		if(racePersister.save(raceForm, bindingResult)) {
			log.info(String.format("Saved race: %s", raceForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", raceForm));
			model.addAttribute("gangType", rulesService.viewGangType(groupId, raceForm.getGangTypeId()));
			return "wh40kskirmish/races_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/race/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_RACE_ID + "}")
	public String viewRace(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_RACE_ID) final Long raceId,
			final Model model) {
		model.addAttribute("race", rulesService.viewRace(groupId, gangTypeId, raceId));
		return "wh40kskirmish/races_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/race/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_RACE_ID + "}/edit", method=RequestMethod.GET)
	public String editRaceForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_RACE_ID) final Long raceId,
			final Model model) {
		log.info(String.format("Viewing edit race form for race %d of gang type %d in group %d", raceId, gangTypeId, groupId));
		final Wh40kSkirmishRaceOutput race = rulesService.viewRace(groupId, gangTypeId, raceId);
		model.addAttribute("gangType", race.getGangType());
		model.addAttribute("raceForm", new Wh40kSkirmishRaceInput(race));
		return "wh40kskirmish/races_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/fightertype/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_RACE_ID + "}/create", method=RequestMethod.GET)
	public String createFighterTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_RACE_ID) final Long raceId,
			final Model model) {
		log.info(String.format("Viewing create fighter type form for race %d of gang type %d of group %d", raceId, gangTypeId, groupId));
		model.addAttribute("race", rulesService.viewRace(groupId, gangTypeId, raceId));
		model.addAttribute("fighterTypeForm", new Wh40kSkirmishFighterTypeInput(groupId, gangTypeId, raceId));
		return "wh40kskirmish/fightertypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/fightertype/save", method=RequestMethod.POST)
	public Object saveFighterTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("fighterTypeForm") final Wh40kSkirmishFighterTypeInput fighterTypeForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(fighterTypeForm.getGroupId())) {
			log.error(String.format("Group ID URL (%d) and form (%d) mismatch when attempting to save fighter type form", groupId, fighterTypeForm.getGroupId()));
			throw new IllegalArgumentException(String.format("Unexpected group ID in fighter type form"));
		}
		if(fighterTypePersister.save(fighterTypeForm, bindingResult)) {
			log.info(String.format("Saved fighter type: %s", fighterTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", fighterTypeForm));
			model.addAttribute("race", rulesService.viewRace(groupId, fighterTypeForm.getGangTypeId(), fighterTypeForm.getRaceId()));
			return "wh40kskirmish/fightertypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/fightertype/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_RACE_ID + "}/{" + ATTR_FIGHTERTYPE_ID + "}")
	public String viewFighterType(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_RACE_ID) final Long raceId,
			@PathVariable(ATTR_FIGHTERTYPE_ID) final Long fighterTypeId,
			final Model model) {
		model.addAttribute("fighterType", rulesService.viewFighterType(groupId, gangTypeId, raceId, fighterTypeId));
		return "wh40kskirmish/fightertypes_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/fightertype/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_RACE_ID + "}/{" + ATTR_FIGHTERTYPE_ID + "}/edit", method=RequestMethod.GET)
	public String editFighterTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_RACE_ID) final Long raceId,
			@PathVariable(ATTR_FIGHTERTYPE_ID) final Long fighterTypeId,
			final Model model) {
		log.info(String.format("Viewing edit fighter type form for fighter type %d of race %d of gang type %d in group %d", fighterTypeId, raceId, gangTypeId, groupId));
		final Wh40kSkirmishFighterTypeOutput fighterType = rulesService.viewFighterType(groupId, gangTypeId, raceId, fighterTypeId);
		model.addAttribute("race", fighterType.getRace());
		model.addAttribute("fighterTypeForm", new Wh40kSkirmishFighterTypeInput(fighterType));
		return "wh40kskirmish/fightertypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorycategory/create", method=RequestMethod.GET)
	public String createTerritoryCategoryForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create territory category form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("territoryCategoryForm", new Wh40kSkirmishTerritoryCategoryInput(groupId));
		return "wh40kskirmish/territorycategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorycategory/save", method=RequestMethod.POST)
	public Object saveTerritoryCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("territoryCategoryForm") final Wh40kSkirmishTerritoryCategoryInput territoryCategoryForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(territoryCategoryForm.getGroupId())) {
			log.error(String.format("Group ID URL (%d) and form (%d) mismatch when attempting to save territory category form", groupId, territoryCategoryForm.getGroupId()));
			throw new IllegalArgumentException(String.format("Unexpected group ID in territory category form"));
		}
		if(territoryCategoryPersister.save(territoryCategoryForm, bindingResult)) {
			log.info(String.format("Saved territory category: %s", territoryCategoryForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", territoryCategoryForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/territorycategories_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/territorycategory/{" + ATTR_TERRITORYCATEGORY_ID + "}")
	public String viewTerritoryCategory(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_TERRITORYCATEGORY_ID) final Long territoryCategoryId,
			final Model model) {
		model.addAttribute("territoryCategory", rulesService.viewTerritoryCategory(groupId, territoryCategoryId));
		return "wh40kskirmish/territorycategories_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorycategory/{" + ATTR_TERRITORYCATEGORY_ID + "}/edit", method=RequestMethod.GET)
	public String editTerritoryCategoryForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_TERRITORYCATEGORY_ID) final Long territoryCategoryId,
			final Model model) {
		log.info(String.format("Viewing edit territory category form for territory category %d in group %d", territoryCategoryId, groupId));
		final Wh40kSkirmishTerritoryCategoryOutput territoryCategory = rulesService.viewTerritoryCategory(groupId, territoryCategoryId);
		model.addAttribute("group", territoryCategory.getGroup());
		model.addAttribute("territoryCategoryForm", new Wh40kSkirmishTerritoryCategoryInput(territoryCategory));
		return "wh40kskirmish/territorycategories_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorytype/{" + ATTR_TERRITORYCATEGORY_ID + "}/create", method=RequestMethod.GET)
	public String createTerritoryTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_TERRITORYCATEGORY_ID) final Long territoryCategoryId,
			final Model model) {
		log.info(String.format("Viewing create territory type form for territory category %d of group %d", territoryCategoryId, groupId));
		model.addAttribute("territoryCategory", rulesService.viewTerritoryCategory(groupId, territoryCategoryId));
		model.addAttribute("territoryTypeForm", new Wh40kSkirmishTerritoryTypeInput(groupId, territoryCategoryId));
		return "wh40kskirmish/territorytypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorytype/save", method=RequestMethod.POST)
	public Object saveTerritoryTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("territoryTypeForm") final Wh40kSkirmishTerritoryTypeInput territoryTypeForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(territoryTypeForm.getGroupId())) {
			log.error(String.format("Group ID URL (%d) and form (%d) mismatch when attempting to save territory type form", groupId, territoryTypeForm.getGroupId()));
			throw new IllegalArgumentException(String.format("Unexpected group ID in territory type form"));
		}
		if(territoryTypePersister.save(territoryTypeForm, bindingResult)) {
			log.info(String.format("Saved territory type: %s", territoryTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", territoryTypeForm));
			model.addAttribute("territoryCategory", rulesService.viewTerritoryCategory(groupId, territoryTypeForm.getTerritoryCategoryId()));
			return "wh40kskirmish/territorytypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/territorytype/{" + ATTR_TERRITORYCATEGORY_ID + "}/{" + ATTR_TERRITORYTYPE_ID + "}")
	public String viewTerritoryType(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_TERRITORYCATEGORY_ID) final Long territoryCategoryId,
			@PathVariable(ATTR_TERRITORYTYPE_ID) final Long territoryTypeId,
			final Model model) {
		model.addAttribute("territoryType", rulesService.viewTerritoryType(groupId, territoryCategoryId, territoryTypeId));
		return "wh40kskirmish/territorytypes_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/territorytype/{" + ATTR_TERRITORYCATEGORY_ID + "}/{" + ATTR_TERRITORYTYPE_ID + "}/edit", method=RequestMethod.GET)
	public String editTerritoryTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_TERRITORYCATEGORY_ID) final Long territoryCategoryId,
			@PathVariable(ATTR_TERRITORYTYPE_ID) final Long territoryTypeId,
			final Model model) {
		log.info(String.format("Viewing edit territory type form for territory type %d of territory category %d in group %d", territoryTypeId, territoryCategoryId, groupId));
		final Wh40kSkirmishTerritoryTypeOutput territoryType = rulesService.viewTerritoryType(groupId, territoryCategoryId, territoryTypeId);
		model.addAttribute("territoryCategory", territoryType.getTerritoryCategory());
		model.addAttribute("territoryTypeForm", new Wh40kSkirmishTerritoryTypeInput(territoryType));
		return "wh40kskirmish/territorytypes_edit";
	}

	// TODO Add skill categories

	// TODO Add skills

	// TODO Add item categories

	// TODO Add item types
}

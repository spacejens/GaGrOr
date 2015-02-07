package com.gagror.service.wh40kskirmish.rules;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.wh40kskirmish.rules.RulesInput;
import com.gagror.data.wh40kskirmish.rules.RulesRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class RulesPersister
extends AbstractPersister<RulesInput, Wh40kSkirmishRulesEntity, GroupEntity> {

	@Autowired
	RulesRepository rulesRepository;

	@Override
	protected void validateForm(final RulesInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected GroupEntity loadContext(final RulesInput form) {
		// Load through rules repository to get group type check
		return rulesRepository.load(form.getGroupId()).getGroup();
	}

	@Override
	protected boolean isCreateNew(final RulesInput form) {
		return false;
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadExisting(final RulesInput form, final GroupEntity context) {
		return context.getWh40kSkirmishRules();
	}

	@Override
	protected void validateFormVsExistingState(
			final RulesInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishRulesEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit rules %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected void updateValues(final RulesInput form, final Wh40kSkirmishRulesEntity entity) {
		entity.setStartingMoney(form.getStartingMoney());
		entity.setCurrencyName(form.getCurrencyName());
	}
}

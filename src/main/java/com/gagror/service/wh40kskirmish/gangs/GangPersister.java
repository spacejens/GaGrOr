package com.gagror.service.wh40kskirmish.gangs;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.DataNotFoundException;
import com.gagror.data.account.AccountRepository;
import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangInput;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionRepository;
import com.gagror.data.wh40kskirmish.rules.gangs.GangTypeEntity;
import com.gagror.service.AbstractIdentifiablePersister;

@Service
@CommonsLog
public class GangPersister
extends AbstractIdentifiablePersister<GangInput, GangEntity, FactionEntity> {

	@Autowired
	FactionRepository factionRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	GangRepository gangRepository;

	@Override
	protected void validateForm(final GangInput form, final BindingResult bindingResult) {
		if(! form.isPersistent()) {
			// Validate that loading can be performed without causing an exception
			accountRepository.load(form.getPlayerId());
		}
	}

	@Override
	protected FactionEntity loadContext(final GangInput form) {
		return factionRepository.load(form.getGroupId(), form.getFactionId());
	}

	@Override
	protected void validateFormVsContext(
			final GangInput form,
			final BindingResult bindingResult,
			final FactionEntity context) {
		for(final GangTypeEntity gangType : context.getGangType().getRules().getGangTypes()) {
			for(final FactionEntity faction : gangType.getFactions()) {
				for(final GangEntity gang : faction.getGangs()) {
					if(gang.getName().equals(form.getName())
							&& ! gang.hasId(form.getId())) {
						form.addErrorNameMustBeUniqueWithinGroup(bindingResult);
					}
				}
			}
		}
	}

	@Override
	protected GangEntity loadExisting(final GangInput form, final FactionEntity context) {
		for(final GangEntity gang : context.getGangs()) {
			if(gang.hasId(form.getId())) {
				return gang;
			}
		}
		throw new DataNotFoundException(String.format(
				"Gang %d (faction %d, gang type %d, group %d)",
				form.getId(),
				context.getId(),
				context.getGangType().getId(),
				context.getGangType().getRules().getGroup().getId()));
	}

	@Override
	protected void validateFormVsExistingState(
			final GangInput form,
			final BindingResult bindingResult,
			final GangEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit gang %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected GangEntity createNew(final GangInput form, final FactionEntity context) {
		return new GangEntity(context, accountRepository.load(form.getPlayerId()));
	}

	@Override
	protected void updateValues(final GangInput form, final GangEntity entity) {
		entity.setName(form.getName());
		entity.setMoney(form.getMoney());
	}

	@Override
	protected GangEntity makePersistent(final GangEntity entity) {
		return gangRepository.persist(entity);
	}
}

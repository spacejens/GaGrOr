package com.gagror.service.wh40kskirmish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gagror.data.wh40kskirmish.gangs.GangEntity;
import com.gagror.data.wh40kskirmish.gangs.GangRepository;
import com.gagror.service.accesscontrol.AbstractPermissionPlayer;

@Component
public class Wh40kSkirmishPermissionGangPlayer extends AbstractPermissionPlayer<GangEntity> {

	@Autowired
	GangRepository gangRepository;

	public Wh40kSkirmishPermissionGangPlayer() {
		super("wh40kskGangPlayer", GangEntity.class);
	}

	@Override
	protected GangEntity loadTarget(final Long id) {
		return gangRepository.load(id);
	}
}

package com.gagror.service.wh40kskirmish.gangs;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagror.data.wh40kskirmish.gangs.FighterEntity;
import com.gagror.data.wh40kskirmish.gangs.FighterViewOutput;

@Service
@Transactional
public class FighterViewService {

	public FighterViewOutput view(final FighterEntity entity) {
		final FighterViewOutput.Builder builder = new FighterViewOutput.Builder(entity);
		// TODO Add fighter type to the fighter view output
		// TODO Add experience level title to the fighter view output
		// TODO Add characteristics to the fighter view output
		return builder.build();
	}
}

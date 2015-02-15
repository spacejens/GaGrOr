package com.gagror.data.wh40kskirmish.gangs;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.gagror.data.AbstractEditableNamedEntity;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.PlayerOwned;
import com.gagror.data.wh40kskirmish.rules.gangs.FighterTypeEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_fighter")
public class FighterEntity extends AbstractEditableNamedEntity implements PlayerOwned {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private GangEntity gang;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private FighterTypeEntity fighterType;

	public FighterEntity(final GangEntity gang, final FighterTypeEntity fighterType) {
		this.gang = gang;
		this.fighterType = fighterType;
		// Add the new entity to the referencing collection
		gang.getFighters().add(this);
	}

	@Override
	public AccountEntity getPlayer() {
		return getGang().getPlayer();
	}

	@Override
	public GroupEntity getGroup() {
		return getGang().getGroup();
	}
}

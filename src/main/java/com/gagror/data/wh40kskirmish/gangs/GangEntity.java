package com.gagror.data.wh40kskirmish.gangs;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractEditableNamedEntity;
import com.gagror.data.account.AccountEntity;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.PlayerOwned;
import com.gagror.data.wh40kskirmish.rules.gangs.FactionEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_gang")
public class GangEntity extends AbstractEditableNamedEntity implements PlayerOwned {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private FactionEntity faction;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private AccountEntity player;

	@OneToMany(mappedBy="gang", fetch=FetchType.LAZY)
	@Getter
	private Set<FighterEntity> fighters;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int money;

	public GangEntity(final FactionEntity faction, final AccountEntity player) {
		this.faction = faction;
		this.player = player;
		// Add the new entity to the referencing collection
		faction.getGangs().add(this);
	}

	@Override
	public GroupEntity getGroup() {
		return getFaction().getGroup();
	}
}

package com.gagror.data.wh40kskirmish.rules.gangs;

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
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupOwned;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_race")
public class RaceEntity extends AbstractEditableNamedEntity implements GroupOwned {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private GangTypeEntity gangType;

	@OneToMany(mappedBy="race", fetch=FetchType.LAZY)
	@Getter
	private Set<FighterTypeEntity> fighterTypes;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int maxMovement;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int maxWeaponSkill;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int maxBallisticSkill;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int maxStrength;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int maxToughness;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int maxWounds;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int maxInitiative;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int maxAttacks;

	@Column(nullable = false, insertable = true, updatable = true)
	@Getter
	@Setter
	private int maxLeadership;

	// TODO Races grant or forbid skills (with optional criteria, such as max/min XP to have skill)

	public RaceEntity(final GangTypeEntity gangType) {
		this.gangType = gangType;
		// Add the new entity to the referencing collection
		gangType.getRaces().add(this);
	}

	@Override
	public GroupEntity getGroup() {
		return getGangType().getGroup();
	}
}

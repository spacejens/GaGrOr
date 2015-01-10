package com.gagror.data.wh40kskirmish;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.gagror.data.AbstractEditableEntity;
import com.gagror.data.group.GroupEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="wh40ksk_rules")
public class Wh40kSkirmishRulesEntity extends AbstractEditableEntity {

	@OneToOne(optional=false)
	@Getter
	GroupEntity group;

	@OneToMany(mappedBy="rules", fetch=FetchType.LAZY)
	@Getter
	private Set<Wh40kSkirmishGangTypeEntity> gangTypes;

	public Wh40kSkirmishRulesEntity(final GroupEntity group) {
		this.group = group;
	}
}

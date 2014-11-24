package com.gagror.data.wh40kskirmish;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
	GroupEntity group;

	public Wh40kSkirmishRulesEntity(final GroupEntity group) {
		this.group = group;
	}
}

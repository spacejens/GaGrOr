package com.gagror.data.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.gagror.data.AbstractEditableEntity;
import com.gagror.data.account.AccountEntity;

@Data
@NoArgsConstructor
@ToString(of={"group", "account", "memberType"}, callSuper=true)
@Entity
@Table(name="groupmember")
@EqualsAndHashCode(of={}, callSuper=true)
public class GroupMemberEntity extends AbstractEditableEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	private GroupEntity group;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	private AccountEntity account;

	@Column(nullable = false)
	private MemberType memberType;
}

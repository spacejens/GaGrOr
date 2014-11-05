package com.gagror.data.group;

import java.util.Set;

import lombok.Data;

@Data
public class GroupInviteInput {

	private Long id;

	private Set<Long> selected;
}

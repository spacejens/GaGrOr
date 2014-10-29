package com.gagror.data.group;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class GroupCreateInput {

	@Size(min=3, max=64)
	private String name;
}

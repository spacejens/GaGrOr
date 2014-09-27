package com.gagror.data.account;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude="password")
public class LoginCredentialsInput {

	private String login;

	private String password;
}

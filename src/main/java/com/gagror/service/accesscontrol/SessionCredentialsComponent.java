package com.gagror.service.accesscontrol;

import lombok.Data;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.gagror.data.account.LoginCredentialsInput;

@Data
@Component
@Scope(value="session",proxyMode= ScopedProxyMode.TARGET_CLASS)
public class SessionCredentialsComponent {

	private LoginCredentialsInput loginCredentials;
}

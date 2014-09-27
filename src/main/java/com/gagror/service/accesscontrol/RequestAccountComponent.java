package com.gagror.service.accesscontrol;

import lombok.Data;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.gagror.data.account.AccountEntity;

@Data
@Component
@Scope(value="request",proxyMode= ScopedProxyMode.TARGET_CLASS)
public class RequestAccountComponent {

	private AccountEntity account;

	private boolean loaded;
}

package com.liaison.service.controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.Logger.ALogger;
import play.mvc.Action.Simple;
import play.mvc.Http.Context;
import play.mvc.Result;

public class OAuthRedirect extends Simple {

	private static final ALogger logger = play.Logger.of("application");
	
	private static final String ISAUTH = "auth.on";

	Config configuration = ConfigFactory.load();

	@Override
	public CompletionStage<Result> call(Context ctx) {

		try {

			if (configuration.getBoolean(ISAUTH)) {
				return CompletableFuture.completedFuture(redirect(routes.UserLoginOAuthController.auth()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return delegate.call(ctx);
	}
}

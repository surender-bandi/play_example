package com.liaison.service.controllers;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.data.FormFactory;
import play.libs.openid.OpenIdClient;
import play.libs.openid.UserInfo;
import play.mvc.Controller;
import play.mvc.Result;

public class OpenIDController extends Controller {

	Config configuration = ConfigFactory.load();
	
	private static final String OPENID = "auth.openid";

    @Inject
    OpenIdClient openIdClient;

    public CompletionStage<Result> loginPost() {

        String url = routes.OpenIDController.openIDCallback().absoluteURL(request());
        CompletionStage<String> redirectUrlPromise =
                openIdClient.redirectURL(configuration.getString(OPENID), url);
        return redirectUrlPromise
                .thenApply(Controller::redirect)
                .exceptionally(throwable ->
                          internalServerError(throwable.getMessage())
        );
    }

    public CompletionStage<Result> openIDCallback() {

        CompletionStage<UserInfo> userInfoPromise = openIdClient.verifiedId();
        CompletionStage<Result> resultPromise = userInfoPromise.thenApply(userInfo ->
                         ok(userInfo.id() + "\n" + userInfo.attributes())
        ).exceptionally(throwable ->
                         internalServerError(throwable.getMessage())
        );
        return resultPromise;
    }
}
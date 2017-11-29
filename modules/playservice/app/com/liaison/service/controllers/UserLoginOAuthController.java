package com.liaison.service.controllers;

import play.libs.oauth.OAuth;
import play.libs.oauth.OAuth.ConsumerKey;
import play.libs.oauth.OAuth.OAuthCalculator;
import play.libs.oauth.OAuth.RequestToken;
import play.libs.oauth.OAuth.ServiceInfo;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.base.Strings;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.inject.Inject;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserLoginOAuthController extends Controller {

    Config configuration = ConfigFactory.load();

    private static final String HOME_TIMELINE_URL = "auth.hometimeline";

    static final ConsumerKey KEY = new ConsumerKey("PZBfr8rAd8c7jdB5qF8z2phhS", "06CEUJvhAYGWblE5Gp2lyCnsiogcrEN1yy125fCfXeetoCjAtf");

    private static final ServiceInfo SERVICE_INFO =
            new ServiceInfo("https://api.twitter.com/oauth/request_token",
                    "https://api.twitter.com/oauth/access_token",
                    "https://api.twitter.com/oauth/authorize",
                    KEY);

    private static final OAuth UASERLOGINOAUTH = new OAuth(SERVICE_INFO);

    private final WSClient ws;

    @Inject
    public UserLoginOAuthController(WSClient ws) {
        this.ws = ws;
    }

    public CompletionStage<Result> homeTimeline() {
        Optional<RequestToken> sessionTokenPair = getSessionTokenPair();
        if (sessionTokenPair.isPresent()) {
            return ws.url(configuration.getString(HOME_TIMELINE_URL))
                    .sign(new OAuthCalculator(UserLoginOAuthController.KEY, sessionTokenPair.get()))
                    .get()
                    .thenApply(result -> ok(result.asJson()));
        }
        return CompletableFuture.completedFuture(redirect(routes.UserLoginOAuthController.auth()));
    }

    public Result auth() {
        String verifier = request().getQueryString("oauth_verifier");
        if (Strings.isNullOrEmpty(verifier)) {
            String url = routes.UserLoginOAuthController.auth().absoluteURL(request());
            RequestToken requestToken = UASERLOGINOAUTH.retrieveRequestToken(url);
            saveSessionTokenPair(requestToken);
            return redirect(UASERLOGINOAUTH.redirectUrl(requestToken.token));
        } else {
            RequestToken requestToken = getSessionTokenPair().get();
            RequestToken accessToken = UASERLOGINOAUTH.retrieveAccessToken(requestToken, verifier);
            saveSessionTokenPair(accessToken);
            return redirect(routes.UserLoginOAuthController.homeTimeline());
        }
    }

    private void saveSessionTokenPair(RequestToken requestToken) {
        session("token", requestToken.token);
        session("secret", requestToken.secret);
    }

    private Optional<RequestToken> getSessionTokenPair() {
        if (session().containsKey("token")) {
            return Optional.ofNullable(new RequestToken(session("token"), session("secret")));
        }
        return Optional.empty();
    }

}
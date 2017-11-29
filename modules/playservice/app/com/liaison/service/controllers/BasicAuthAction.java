package com.liaison.service.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.Logger.ALogger;
import play.mvc.Action.Simple;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;

/**
 * Java class for do the basic authentication
 * 
 */
public class BasicAuthAction extends Simple {

    private static final ALogger logger = play.Logger.of("application");

    Config configuration = ConfigFactory.load();

    private static final String AUTHORIZATION = "Authorization";

    private static final String BASIC = "Basic ";

    private static final String INVALID_USER = "Username or password invalid";

    private static final String NEEDS_AUTH = "Needs authorization";

    private static final String INVALID_HEADER = "Invalid auth header";

    private static final String USER = "auth.user";

    private static final String PASSWORD = "auth.pass";

    @Override
    public CompletionStage<Result> call(Context ctx) {

        logger.info("Entering the basic authentication");
        Optional<String> authHeader = ctx.request().header(AUTHORIZATION);
        if (StringUtils.isEmpty(authHeader.get())) {
            return CompletableFuture.completedFuture(status(Http.Status.UNAUTHORIZED, NEEDS_AUTH));
        }

        String[] credentials;
        try {
            credentials = parseAuthHeader(authHeader.get());
        } catch (Exception e) {
            logger.warn("Cannot parse basic auth info", e);
            return CompletableFuture.completedFuture(status(Http.Status.FORBIDDEN, INVALID_HEADER));
        }
        String userName = credentials[0];
        boolean loginCorrect = checkLogin(userName, credentials[1]);

        if (!loginCorrect) {
            logger.warn("Incorrect basic auth login, username=" + userName);
            return CompletableFuture.completedFuture(status(Http.Status.FORBIDDEN, "Forbidden"));
        } else {
            ctx.args.put("USER", userName);
            logger.info("Successful basic auth login, username=" + userName);
            return delegate.call(ctx);
        }
    }

    /**
     * Helper method to validate the credentials
     * 
     * @param username
     * @param password
     * @return boolean
     */
    private boolean checkLogin(String username, String password) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException(INVALID_USER);
        }
        // We can do our own authentication check in our services
        return username.equals(configuration.getString(USER)) && password.equals(configuration.getString(PASSWORD));
    }

    /**
     * Helper method to decode the user and password
     * 
     * @param authHeader
     * @return string[]
     * @throws UnsupportedEncodingException
     */
    private String[] parseAuthHeader(String authHeader) throws UnsupportedEncodingException {

        if (!authHeader.startsWith(BASIC)) {
            throw new IllegalArgumentException(NEEDS_AUTH);
        }

        String[] credString;
        String auth = authHeader.substring(6);
        byte[] decodedAuth = new Base64().decode(auth);
        credString = new String(decodedAuth, "UTF-8").split(":", 2);
        if (credString.length != 2) {
            throw new IllegalArgumentException(NEEDS_AUTH);
        }
        return credString;
    }
}

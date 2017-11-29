package com.liaison.service.controllers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.HttpMethod;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.Logger.ALogger;
import play.mvc.Controller;
import play.mvc.Result;

public class PlayAkkaHTTPController extends Controller {

    private static final ALogger logger = play.Logger.of("application");

    Config configuration = ConfigFactory.load();

    private static final String HTTP_URL = "remoteactor.url";

    public Result putMessageToAkkaHTTP() throws IOException {

        HttpURLConnection connection = null;
        try {

            String akkaUrl = configuration.getString(HTTP_URL);
            logger.info("The requested URL is " + akkaUrl);

            URL url = new URL(akkaUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(HttpMethod.PUT);

            OutputStreamWriter response = new OutputStreamWriter(connection.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return ok(connection.getResponseMessage());
    }
}

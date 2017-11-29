package com.liaison.service.controllers;

import play.Logger.ALogger;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Class to check the play error handler.
 *
 */
public class ErrorController extends Controller {

    private static final ALogger logger = play.Logger.of("application");

    public Result showError() {

        String value = "DEFAULT_ERROR";
        try {
            logger.info("Value is: " + value);
            Integer.parseInt(value);
            return ok("Returned + " + value);
        } catch (Exception e) {
            throw new RuntimeException("Invalid number. Please enter valid number");
        }
    }
}

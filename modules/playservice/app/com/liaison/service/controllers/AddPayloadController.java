package com.liaison.service.controllers;

import com.liaison.service.models.Payload;
import com.liaison.service.models.PayloadStore;

import play.Logger.ALogger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Controller class to add the payload
 *
 */
public class AddPayloadController extends Controller {

    private static final ALogger logger = play.Logger.of("application");

    public Result addPayload() {

        JsonNode jsonMessage = request().body().asJson();
        Payload payload = Json.fromJson(jsonMessage, Payload.class);
        PayloadStore.getInstance().addPayload(payload);

        logger.info("Payload TestPayloadData added successful");
        return ok(payload.getPayloadData() + " added succesfully");
    }
}

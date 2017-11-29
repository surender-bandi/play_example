package com.liaison.service.controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liaison.service.models.Payload;
import com.liaison.service.models.PayloadStore;
import com.liaison.service.Util;

import play.Logger.ALogger;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controller class to list payload
 *
 */
public class ReadPayloadController extends Controller {

    ALogger log = play.Logger.of("application");
    public Result listPayload() {

        List<Payload> result = PayloadStore.getInstance().getPayloadData();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
        log.info("payload read success");
        return ok(Util.createResponse(jsonData, true));
    }
}

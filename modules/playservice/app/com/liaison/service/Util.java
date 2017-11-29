package com.liaison.service;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Util class for responses
 *
 */
public class Util {

    @SuppressWarnings("deprecation")
	public static ObjectNode createResponse(Object response, boolean ok) {
         
        ObjectNode result = Json.newObject();
        result.put("Success", ok);
        if (response instanceof String) {
            result.put("body", (String) response);
        }
        else {
            result.put("body", (JsonNode) response);
        }
 
        return result;
    }
}
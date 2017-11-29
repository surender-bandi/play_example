package com.liaison.service.controllers;

import com.liaison.service.models.PayloadStore;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controller class to list payload
 *
 */
public class RemovePayloadController extends Controller {

    public Result removePayload() {
    	PayloadStore.reset();
        return ok("Payloads Deleted successfully");
    }
}

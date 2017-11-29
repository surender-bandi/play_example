package com.liaison.service.play.nucleus;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.DELETE;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import org.junit.Test;

import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Http.Status;
import play.test.WithApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.liaison.service.models.Payload;

public class AddPayloadControllerTest extends WithApplication {

	private static final String PAYLOAD = "Payload#123_data";

	/**
	 * Method to test adding the payload data
	 */
    @Test
    public void testAddPayload() {

        Payload payload = new Payload();
        payload.setPayloadId(1);
        payload.setPayloadName(PAYLOAD);
        JsonNode json = Json.toJson(payload);

        // Creating Request to send
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(json)
                .uri("/addPayload");

        Result result = route(app, request);
        assertThat(result.status(), equalTo(Status.OK));

        final String body = contentAsString(result);
        assertThat(body, containsString(payload.getPayloadName() + " added succesfully"));
	}

    /**
     * Method to test the list payload data
     */
    @Test
    public void testListPayload() {

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/list");

        Result result = route(app, request);
        final String body = contentAsString(result);
        assertThat(body, containsString(PAYLOAD));
    }

    /**
     * Method to remove/delete the payload data
     */
    @Test
    public void testRemovePayload() {

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/");

        Result result = route(app, request);
        final String body = contentAsString(result);
        assertThat(body, containsString("Deleted"));
    }
}

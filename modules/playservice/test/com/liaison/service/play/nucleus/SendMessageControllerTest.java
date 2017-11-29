package com.liaison.service.play.nucleus;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.POST;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.liaison.service.models.Message;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Http.Status;
import play.mvc.Result;
import play.test.WithApplication;

public class SendMessageControllerTest extends WithApplication {

	private static final String MESSAGE = "Test Message";

	/**
	 * Method to test the sending messages in akka.
	 */
	@Test
	public void testSendMessage() {

        Message message = new Message();
        message.setMsgValue(MESSAGE);
        JsonNode json = Json.toJson(message);

        // Creating Request to send
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(json)
                .uri("/message");

        Result result = route(app, request);
        assertThat(result.status(), equalTo(Status.OK));

        final String body = contentAsString(result);
        assertThat(body, containsString("Post message " + message.getMsgValue() + " success"));
	}
}

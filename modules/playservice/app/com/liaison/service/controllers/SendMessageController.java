package com.liaison.service.controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.fasterxml.jackson.databind.JsonNode;
import com.liaison.service.models.Message;
import com.liaison.service.models.ReceivingActor;
import com.typesafe.config.ConfigFactory;

import play.Logger.ALogger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Simple controller class to post the message
 *
 */
public class SendMessageController extends Controller {

    private static final ALogger logger = play.Logger.of("application");

    public Result sendMessage() {

        JsonNode jsonMessage = request().body().asJson();
        Message message = Json.fromJson(jsonMessage, Message.class);

        logger.info("Message to send: " + message.getMsgValue());
        final ActorSystem system= ActorSystem.create("AkkaMessageSystem", ConfigFactory.load("application-dev-int.conf"));
        final ActorRef receivingActor =system.actorOf(Props.create(ReceivingActor.class), "ReceivingActor");
        receivingActor.tell(message, ActorRef.noSender());
        return ok("Post message " + message.getMsgValue() + " success");
    }
}

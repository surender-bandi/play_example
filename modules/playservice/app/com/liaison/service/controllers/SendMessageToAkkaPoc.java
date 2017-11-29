package com.liaison.service.controllers;

import javax.inject.Inject;

import play.Logger.ALogger;
import play.mvc.Controller;
import play.mvc.Result;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class SendMessageToAkkaPoc extends Controller {

	private static final ALogger logger = play.Logger.of("application");

	private ActorRef playMockActor;

	@Inject 
	public SendMessageToAkkaPoc(ActorSystem system) {
		playMockActor = system.actorOf(Props.create(PlayAkkaActor.class), "playMockActor");
    }

	public Result sendPlayMessage() {
		logger.info("Entering in to send message");
		playMockActor.tell(200, ActorRef.noSender());
		return ok("Successfully sent");
	}
}

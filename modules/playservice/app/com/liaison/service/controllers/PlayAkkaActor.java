package com.liaison.service.controllers;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.Logger.ALogger;
import akka.actor.AbstractActor;
import akka.actor.ActorSelection;

public class PlayAkkaActor extends AbstractActor {

	private static final ALogger logger = play.Logger.of("application");

	Config configuration = ConfigFactory.load();

	private static final String REMOTE_ACTOR = "remoteactor.system";

	@Override
	public Receive createReceive() {

		return receiveBuilder()
				.match(Integer.class, value -> {
					ActorSelection selection= getContext()
							.actorSelection(configuration.getString(REMOTE_ACTOR));
					selection.tell(value, self());
				})
				.match(String.class, response -> {
					logger.info("Received response: " + response);
				}).build();
	}

}

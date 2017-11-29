package com.liaison.service.models;

import play.Logger.ALogger;
import akka.actor.AbstractActor;

/**
 * Receiving actor class to receive the message
 *
 */
public class ReceivingActor extends AbstractActor {

    private static final ALogger logger = play.Logger.of("application");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
          .match(Message.class, message -> {
              logger.info("Received String message is : {}", message.getMsgValue());
          })
          .matchAny(anyObject -> {
              logger.warn("Received unknown message");
          })
          .build();
    }
}

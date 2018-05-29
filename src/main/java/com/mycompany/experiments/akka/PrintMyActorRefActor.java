package com.mycompany.experiments.akka;


import akka.actor.AbstractActor;
import akka.actor.AbstractActor.Receive;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

class PrintMyActorRefActor extends AbstractActor {
  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .matchEquals("printit", p -> {
          ActorRef secondRef = getContext().actorOf(Props.empty(), "second-actor");
          System.out.println("Second: " + secondRef);
        })
        .build();
  }
}

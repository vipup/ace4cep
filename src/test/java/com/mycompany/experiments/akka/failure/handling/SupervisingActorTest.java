package com.mycompany.experiments.akka.failure.handling;

import static org.junit.Assert.*;

import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class SupervisingActorTest {

	@Test
	public void test() {
		ActorSystem system = ActorSystem.create("testSystem");
		ActorRef supervisingActor = system.actorOf(Props.create(SupervisingActor.class), "supervising-actor");
		supervisingActor.tell("failChild", ActorRef.noSender());
		
		assertTrue(true);
	}

}

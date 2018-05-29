package com.mycompany.experiments.akka.lifecycle;

import static org.junit.Assert.*;

import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class StartStopActor1Test {

	@Test
	public void test() {
		ActorSystem system = ActorSystem.create("testSystem");
		ActorRef first = system.actorOf(Props.create(StartStopActor1.class), "first");
		first.tell("stop", ActorRef.noSender());
		assertTrue(true);
	}

}

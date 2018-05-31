package com.mycompany.experiments.akka.failure.handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.AbstractActor;
import eu.blky.cep.listeners.Defaulistener;

class SupervisedActor extends AbstractActor {
	private boolean failed;
	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(SupervisedActor.class);

	@Override
	public void preStart() {
		LOG.info("supervised actor started");
	}

	@Override
	public void postStop() {
		LOG.info("supervised actor stopped");
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.matchEquals("fail", f -> {
				LOG.info("supervised actor fails now");
				this.setFailed(true); 
				throw new Exception("I failed!");		
			})
			.build();
	}

	/**
	 * @return the failed
	 */
	public boolean isFailed() {
		return failed;
	}

	/**
	 * @param failed the failed to set
	 */
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
}
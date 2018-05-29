package com.mycompany.experiments.akka.failure.handling;

import akka.actor.AbstractActor;

class SupervisedActor extends AbstractActor {
	private boolean failed;

	@Override
	public void preStart() {
		System.out.println("supervised actor started");
	}

	@Override
	public void postStop() {
		System.out.println("supervised actor stopped");
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.matchEquals("fail", f -> {
				System.out.println("supervised actor fails now");
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
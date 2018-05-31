package eu.blky.cep.kafka;

public class KafkaSession {

	private String id;

	public KafkaSession(String idPar) {
		this.setId(idPar);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}

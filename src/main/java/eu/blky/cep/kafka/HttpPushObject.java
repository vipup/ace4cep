package eu.blky.cep.kafka;

public class HttpPushObject {

	private long id;

	public HttpPushObject(long id) {
		this.setId(id);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

}

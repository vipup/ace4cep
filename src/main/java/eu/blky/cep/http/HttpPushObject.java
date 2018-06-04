package eu.blky.cep.http;

public class HttpPushObject {

	private long id;
	private double value;

	public HttpPushObject(long id) {
		this.setId(id);
	}

	public HttpPushObject(long id2, double value) {
		this(id2);
		this.setValue(value);
	}

	private void setValue(double value) {
		this.value = value;
		
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

	public double getValue() {
		return value;
	}

}

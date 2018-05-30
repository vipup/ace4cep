package com.mycompany;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class MyKafkaEvent {
 
	private String topic;

	private int partition;

	private long offset;

	private String key;

	private String value;
  
	public MyKafkaEvent(ConsumerRecord<String, String> record) {
		if (record == null) return;
		this.topic = record.topic();
		this.partition = record.partition();
		this.offset = record.offset();
		this.key = record.key();
		this.value = record.value();
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public int getPartition() {
		return partition;
	}
	public void setPartition(int partition) {
		this.partition = partition;
	}
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}

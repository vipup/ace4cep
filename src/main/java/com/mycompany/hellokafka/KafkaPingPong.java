package com.mycompany.hellokafka;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaPingPong {
	private final static String PING_TOPIC_NAME = "ping_topic_name";
	private final static String PONG_TOPIC_NAME = "pong_topic_name";
	// consume messages
	Consumer<String, String> PING_consumer = KafkaConsumerExample.createConsumer();
	Consumer<String, String> PONG_consumer = KafkaConsumerExample.createConsumer();

	// produce a test message
	// if u run this multiple times ... u will have multiple messages in the
	// test_topic topic (as would be expected)
	Producer<String, String> producer = KafkaProducerExample.createProducer();

	public KafkaPingPong() {
 
        // subscribe to the 2 topics
		PING_consumer.subscribe(Collections.singletonList(PING_TOPIC_NAME));
		PONG_consumer.subscribe(Collections.singletonList(PONG_TOPIC_NAME));
		 
		 
        		
	}
	
	public void ping (String text) throws InterruptedException, ExecutionException {
		send(PING_TOPIC_NAME, text);
		receive(PONG_consumer);
	}
 

	
	public void send(String sTOPIC, String text) throws InterruptedException, ExecutionException{
		ProducerRecord<String, String> recordToSend = new ProducerRecord<String, String>(sTOPIC, "message",
                text + " , timeInMillis=" + System.currentTimeMillis());
		//
        // synchronous send.... get() waits for the computation to
        // finish
        RecordMetadata rmd = producer.send(recordToSend).get();
        System.out.printf("Message Sent ==>> topic = %s, partition = %s, offset = %d\n", rmd.topic(),
                rmd.partition(), rmd.offset());		
		
	}
	
	public void receive(Consumer<String, String>  consumer) {

        try {
            // loop forever (hmmmmm)
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                // print the messages received
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf(
                            "Message received ==> topic = %s, partition = %s, offset = %d, key = %s, value = %s\n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }

            }
        } finally {
            consumer.close();
        }		
	}

}

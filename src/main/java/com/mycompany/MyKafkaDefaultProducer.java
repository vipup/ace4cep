package com.mycompany;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;
 
 
@Component
public class MyKafkaDefaultProducer {
	public void send(String text) throws InterruptedException, ExecutionException {
		Producer<String, String> producer = createProducer();
		ProducerRecord<String, String> recordToSend = 
        		new ProducerRecord<String, String>(MyKafkaEvent.TOPIC_NAME, "message",
        				text );
  
            // synchronous send.... get() waits for the computation to
            // finish
            RecordMetadata rmd = producer.send(recordToSend).get();
            System.out.printf("Message Sent ==>> topic = %s, partition = %s, offset = %d\n", rmd.topic(),
                    rmd.partition(), rmd.offset());
 	
	}
	
    static Producer<String, String> createProducer() {
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "localhost:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<String, String>(kafkaProps);
    }	

}

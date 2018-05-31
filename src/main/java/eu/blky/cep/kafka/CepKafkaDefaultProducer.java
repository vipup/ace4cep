package eu.blky.cep.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component; 

@Component
public class CepKafkaDefaultProducer {
	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(CepKafkaDefaultProducer.class);
 
	Producer<String, String> producer = createProducer();
	
	public void send(String text) throws InterruptedException, ExecutionException {
		
		ProducerRecord<String, String> recordToSend = 
        		new ProducerRecord<String, String>(KafkaDefaultEvent.TOPIC_NAME, "message",
        				text );
  
            // synchronous send.... get() waits for the computation to
            // finish
            RecordMetadata rmd = producer.send(recordToSend).get();
            LOG.debug("Message Sent ==>> topic = {}, partition = {}, offset = {}\n", rmd.topic(),
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

package eu.blky.cep.kafka;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.espertech.esper.client.EPRuntime;

import eu.blky.cep.listeners.Defaulistener; 

@Component
public class CepKafkaDefaultConsumer { 
	static final ThreadGroup t1 = new ThreadGroup("always running MyKafkaReaderThread#"+System.currentTimeMillis() );
	static int counter = 0;
	public CepKafkaDefaultConsumer( ) {
		
		initConsumer();
	}
	@PreDestroy
	private void cleanUp() {
		this.active = false;
	}
	
	ThreadFactory highPrioFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {				
				Runnable arg1 = r;
				String arg2 = "highPrioFactory#"+(counter++);
				Thread retval = new Thread(t1, arg1, arg2);
				retval.setDaemon(false);
				retval.setPriority(Thread.MAX_PRIORITY-1);
				return retval ;
			}
		};
	ScheduledExecutorService neverendingThread = Executors.newSingleThreadScheduledExecutor(highPrioFactory );
		
	 
	
	private boolean active=true;
	//TODO - 
	private static final ArrayList<EPRuntime> listeners = new ArrayList<EPRuntime>();
	
	public void addListener(EPRuntime epRuntime ) {
		LOG.debug("add listener: {}",epRuntime);
		this.getListeners() .add(epRuntime);
	}
	
	public void initConsumer() {
		
		
	    Runnable commandTmp = new Runnable() { 
 
	        @Override
	        public void run() {
 
	        	try {
	                // consume messages
	                Consumer<String, String> consumer = createConsumer();

	                // subscribe to the test topic
	                consumer.subscribe(Collections.singletonList(KafkaDefaultEvent.TOPIC_NAME));
	                try {
	                    // loop forever (hmmmmm)
	                    while (active) {
	                        ConsumerRecords<String, String> records = consumer.poll(100);
	                        // print the messages received
	                        for (ConsumerRecord<String, String> record : records) {
	                        	LOG.error(
	                                    "Message rEcEivEd ==> topic = {}, partition = {}, offset = {}, key = {}, value = {}\n",
	                                    record.topic(), record.partition(), record.offset(), record.key(), record.value());

	                            KafkaDefaultEvent eTmp = new KafkaDefaultEvent(record);
	                            
								List<EPRuntime> listeners2 = getListeners();
								for(EPRuntime rt : listeners2) {
									try {
										rt.sendEvent(eTmp);
										LOG.trace("distributed to:{} <{}", rt, record.value() );
									}catch(Exception e) {
										LOG.error("for(EPRuntime rt : listeners2) {;", e );
									}
	                            }
	                        }

	                    }
	                } finally {
	                    consumer.close();
	                } 
	        	}catch(Throwable e) {
	        		LOG.error("rt.sendEvent(theEvent , SENSOR_EVENT);", e );
	        		
	        	}
	        }

 
			
		    Consumer<String, String> createConsumer() {
		        Properties kafkaProps = new Properties();
		        kafkaProps.put("bootstrap.servers", "localhost:9092");
		        kafkaProps.put("group.id", "test_consumer_group");
		        kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		        kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		        return new KafkaConsumer<String, String>(kafkaProps);
		    }			
	    };
		long initialDelay = 1;
 
		TimeUnit unit = TimeUnit.SECONDS;
		// only once
		shutdownHook = neverendingThread.schedule(commandTmp, initialDelay, unit);
		
		
	}
	ScheduledFuture<?> shutdownHook ;
	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(Defaulistener.class);
	public void stopMonitoring( ) {
		LOG.debug("stopMonitoring!");
		active = false;
		boolean mayInterruptIfRunning = true;
		shutdownHook.cancel(mayInterruptIfRunning );
	}

	public void removeListener(EPRuntime cepRT) {
		LOG.debug("delete listener: {}",cepRT);
		listeners.remove(cepRT);
	}

	/**
	 * @return the listeners
	 */
	public List<EPRuntime> getListeners() {
		return listeners;
	}

}

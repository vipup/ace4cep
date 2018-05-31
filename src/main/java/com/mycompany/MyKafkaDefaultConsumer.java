package com.mycompany;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import com.espertech.esper.client.EPRuntime; 

@Component
public class MyKafkaDefaultConsumer {
	public static String KAFKA_HOOK = "KAFKA_HOOK";
	static final ThreadGroup t1 = new ThreadGroup("always running MyKafkaThread#"+System.currentTimeMillis() );
 
	public MyKafkaDefaultConsumer( ) {
		initConsumer();
	}

	ThreadFactory highPrioFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {				
				Runnable arg1 = r;
				String arg2 = "highPrioFactory";
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
	                consumer.subscribe(Collections.singletonList(MyKafkaEvent.TOPIC_NAME));
	                try {
	                    // loop forever (hmmmmm)
	                    while (active) {
	                        ConsumerRecords<String, String> records = consumer.poll(100);
	                        // print the messages received
	                        for (ConsumerRecord<String, String> record : records) {
	                            System.out.printf(
	                                    "Message rEcEivEd ==> topic = %s, partition = %s, offset = %d, key = %s, value = %s\n",
	                                    record.topic(), record.partition(), record.offset(), record.key(), record.value());

	                            MyKafkaEvent eTmp = new MyKafkaEvent(record);
	                            
								List<EPRuntime> listeners2 = getListeners();
								for(EPRuntime rt : listeners2) {
									try {
										rt.sendEvent(eTmp);
									}catch(Exception e) {
										e.printStackTrace();
									}
	                            }
	                        }

	                    }
	                } finally {
	                    consumer.close();
	                } 
	        	}catch(Throwable e) {
	        		e.printStackTrace();
	        		
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
	public void stopMonitoring( ) {
		active = false;
	}

	public void removeListener(EPRuntime cepRT) {
		//TODO - listeners.remove(cepRT);
	}

	/**
	 * @return the listeners
	 */
	public List<EPRuntime> getListeners() {
		return listeners;
	}

}

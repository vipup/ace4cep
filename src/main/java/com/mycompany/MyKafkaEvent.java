package com.mycompany;
 
import java.util.Collections; 
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import com.espertech.esper.client.EPRuntime;
import com.mycompany.hellokafka.KafkaConsumerExample;

public class MyKafkaEvent {
	static final ThreadGroup t1 = new ThreadGroup("always running MyEventThread" );

	private EPRuntime rt;

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
	
	public void startMonitoring(EPRuntime epRuntime) {
		rt = epRuntime; 
		
	    Runnable commandTmp = new Runnable() { 
 
	        @Override
	        public void run() {
 
	        	try {
	                // consume messages
	                Consumer<String, String> consumer = KafkaConsumerExample.createConsumer();

	                // subscribe to the test topic
	                consumer.subscribe(Collections.singletonList(KafkaConsumerExample.TOPIC_NAME));
	                try {
	                    // loop forever (hmmmmm)
	                    while (active) {
	                        ConsumerRecords<String, String> records = consumer.poll(100);
	                        // print the messages received
	                        for (ConsumerRecord<String, String> record : records) {
	                            System.out.printf(
	                                    "Message received ==> topic = %s, partition = %s, offset = %d, key = %s, value = %s\n",
	                                    record.topic(), record.partition(), record.offset(), record.key(), record.value());

	        	        		MyKafkaEvent eTmp = new MyKafkaEvent(record);
	        	        		rt.sendEvent(eTmp);
	                        }

	                    }
	                } finally {
	                    consumer.close();
	                } 
	        	}catch(Throwable e) {
	        		e.printStackTrace();
	        		
	        	}
	        }
	    };
		long initialDelay = 1;
		long period = 1;
		TimeUnit unit = TimeUnit.SECONDS;
		shutdownHook = neverendingThread.scheduleAtFixedRate(commandTmp, initialDelay, period, unit);
		
		
	}
	ScheduledFuture<?> shutdownHook ;
	public void stopMonitoring( ) {
		active = false;
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

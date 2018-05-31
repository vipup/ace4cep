package com.mycompany;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPRuntime;

import eu.blky.cep.listeners.Defaulistener;

public class Sensor {
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
	
	
	public  static final String SENSOR_EVENT = "SensorEvent";
	final ThreadGroup t1 = new ThreadGroup("always running SensorThread" );

	public static Sensor getInstance() {
		return new Sensor();		
	}

	private EPRuntime rt;
	Random random = new Random();
	String[] sensors = "s1,s2,s3,s4,s5,s6".split(",");
	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(Sensor.class); 

	public void startMonitoring(EPRuntime epRuntime) {
		this.rt = epRuntime;
		

	    Runnable commandTmp = new Runnable() { 
	    	int checkCount =0;
	        @Override
	        public void run() {
	        	checkCount++; 
	        	double temperature = random.nextDouble() * 35 + 60;
	        	Map<String, Object>  theEvent = new LinkedHashMap<String, Object>();
	        	theEvent.put("temperature", temperature);
	        	int nextInt = random.nextInt(sensors.length);
				theEvent.put("sensor", sensors[nextInt]);
	        	// com.espertech.esper.client.EPException: Event type named 'SensorEvent' has not been defined or is not a Map event type, the name 'SensorEvent' has not been defined as an event type
	        	//theEvent.put("checkCount", checkCount );
				
	        	try {
	        		Thread.sleep(1000*nextInt);
	        		rt.sendEvent(theEvent , SENSOR_EVENT);
	        	}catch(Throwable e) {
	        		LOG.error("rt.sendEvent(theEvent , SENSOR_EVENT);", e );
	        		
	        	}
	        }
	    };
		long initialDelay = 1;
		long period = 1;
		TimeUnit unit = TimeUnit.SECONDS;
		neverendingThread.scheduleAtFixedRate(commandTmp, initialDelay, period, unit);	
	}

	public void stop() {
		neverendingThread.shutdown();		
	} 
}

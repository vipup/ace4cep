package com.mycompany;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import com.espertech.esper.client.EPRuntime;

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

	public void startMonitoring(EPRuntime epRuntime) {
		this.rt = epRuntime;
		

	    Runnable commandTmp = new Runnable() { 
	    	int checkCount =0;
	        @Override
	        public void run() {
	        	checkCount++; 
	        	double temperature = random.nextDouble() * 10 + 80;
	        	Map<String, Object>  theEvent = new LinkedHashMap<String, Object>();
	        	theEvent.put("temperature", temperature);
	        	theEvent.put("sensor", sensors[random.nextInt(sensors.length)]);
	        	// com.espertech.esper.client.EPException: Event type named 'SensorEvent' has not been defined or is not a Map event type, the name 'SensorEvent' has not been defined as an event type
	        	//theEvent.put("checkCount", checkCount );
	        	try {
	        		rt.sendEvent(theEvent , SENSOR_EVENT);
	        	}catch(Throwable e) {
	        		e.printStackTrace();
	        		
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

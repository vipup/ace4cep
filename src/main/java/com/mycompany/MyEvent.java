package com.mycompany;
 
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import com.espertech.esper.client.EPRuntime;

public class MyEvent {
	static final ThreadGroup t1 = new ThreadGroup("always running MyEventThread" );

	static private EPRuntime rt;

	private int somefield;

	private double value;
	private float e;
	String type;
	private int size;
	private long eventtime;
	private short port;
	private String ip_src;
	private String customer;
	private int uid;
	

	private static final String[] types= {"A","b","C","d","e","f","g","h","e","j","k","l","m","n","o","p"};
	

	public MyEvent(int checkCount) {
		this.somefield = checkCount;
		this.value = checkCount % 10;
		this.e = (float) (10001.10001 * (float)value); 
		this.type = types[somefield%types.length];
		this.eventtime = System.currentTimeMillis();
		this.size = checkCount*checkCount %types.length;
		this.port = (short) checkCount;
		this.ip_src = ""+checkCount%255+"."+checkCount%127+"."+checkCount%65+"."+checkCount%31;
		this.setCustomer("Cu"+type);
		this.setUid(("U"+customer+type+eventtime+somefield).hashCode());
	}

	public static void startMonitoring(EPRuntime epRuntime) {
		rt = epRuntime;
		
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
		
	    Runnable commandTmp = new Runnable() { 
	    	int checkCount =0;
	        @Override
	        public void run() {
	        	checkCount++;  
	        	// com.espertech.esper.client.EPException: Event type named 'SensorEvent' has not been defined or is not a Map event type, the name 'SensorEvent' has not been defined as an event type
	        	//theEvent.put("checkCount", checkCount );
	        	try {
	        		MyEvent eTmp = new MyEvent(checkCount);
	        		rt.sendEvent(eTmp);
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

	public int getSomefield() {
		return somefield;
	}

	public void setSomefield(int somefield) {
		this.somefield = somefield;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public float getE() {
		return e;
	}

	public void setE(float e) {
		this.e = e;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getEventtime() {
		return eventtime;
	}

	public void setEventtime(long eventtime) {
		this.eventtime = eventtime;
	}

	public short getPort() {
		return port;
	}

	public void setPort(short port) {
		this.port = port;
	}

	public String getIp_src() {
		return ip_src;
	}

	public void setIp_src(String ip_src) {
		this.ip_src = ip_src;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

}

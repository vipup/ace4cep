package eu.blky.cep.weso.ace4cep;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * stop all statemens, that reached myMaxPerSecond updates/sec
 * 
 * @author i1
 *
 */
public class ThroughputQuotedListener implements UpdateListener {

	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(ThroughputQuotedListener.class);
	private EPStatement statement;
	private AtomicInteger callCount = new AtomicInteger(0);
	private long lastUpdate = System.currentTimeMillis();
	private int myMaxPerSecond = 1000;

	public ThroughputQuotedListener(EPStatement stPar, int maxPerSec) {
		this(stPar);
		this.myMaxPerSecond = maxPerSec;
	}
	public ThroughputQuotedListener(EPStatement stPar) {
		this.statement = stPar;
		this.statement .addListener(this);
		System.out.println("TODO this.statement .addListener(this);this.statement .addListener(this);this.statement .addListener(this);this.statement .addListener(this);");
	}

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		LOG.trace("{} {} ", newEvents, oldEvents);
		if(checkCondition()) {
			this.stop();
		}
		
	}
	
	private boolean checkCondition() {
		int cc = callCount.incrementAndGet();

		if (System.currentTimeMillis() > (lastUpdate +1000)  ){
			callCount.set(0);
		}else if (cc > myMaxPerSecond ) { // more then 1000 per sec
			return true;
		}
		lastUpdate = System.currentTimeMillis();
		return false;
	}

	final private void stop() {
		System.out.println(""+(System.currentTimeMillis() -lastUpdate)+ "STOOOOOOOOOOOOOOOOOP !! reached:: ["+callCount.get()+"]/sec! DANGEROUS STATMENT!::::"+statement.getName()+":::"+statement.getText());
		statement.stop();
	}

	public String toString() {		
		String retval = "_"+this.myMaxPerSecond;
		return retval ;
	}

}

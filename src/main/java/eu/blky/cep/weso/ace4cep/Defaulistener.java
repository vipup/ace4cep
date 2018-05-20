package eu.blky.cep.weso.ace4cep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;
 

public class Defaulistener implements UpdateListener {

	private Messenger sender;
	private int updateCounter;
	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(Defaulistener.class);

	public Defaulistener(Messenger proxyTmp) {
		this.sender = proxyTmp;
	}

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		
		updateCounter++;
		for (Object e:newEvents) { 
			MapEventBean eBean = (MapEventBean)e;
			double value = 0;
			//if (uuid==17)
			for (String properyName : eBean.getProperties().keySet())
			try {
				LOG.trace("{}",  eBean );				
				
				sender.sendMessage("-##"+updateCounter+"##"+ properyName +" == '"+ value+"'");
			 
			}catch(Exception ex) {
				LOG.trace("{}", ex);
			}
		}		
	}
}

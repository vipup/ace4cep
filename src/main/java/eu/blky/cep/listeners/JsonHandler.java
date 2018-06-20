package eu.blky.cep.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;

public class JsonHandler implements UpdateListener {

	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(Defaulistener.class);

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		for (Object e:newEvents) { 
			MapEventBean eBean = (MapEventBean)e;
			for (String properyName : eBean.getProperties().keySet()) {
				try {
					LOG.trace("{} :== {}",  eBean , eBean.getProperties().get(properyName));	
				}catch(Exception ex) {
					// ignore all
				}
			}
		}
	}

}

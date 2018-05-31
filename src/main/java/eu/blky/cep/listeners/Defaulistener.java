package eu.blky.cep.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;

import eu.blky.cep.weso.ace4cep.Messenger;
 

public class Defaulistener implements UpdateListener {

	private Messenger sender;
	private int updateCounter;
	static private int uidcounter;
	private int uid = uidcounter++;
	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(Defaulistener.class);

	public Defaulistener(Messenger proxyTmp) {
		this.sender = proxyTmp;
	}

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		
		updateCounter++;
		String suffix ="";
		String retval =""+uid+"^"+updateCounter+":[";
		for (Object e:newEvents) { 
			MapEventBean eBean = (MapEventBean)e;
			
			//if (uuid==17)
			for (String properyName : eBean.getProperties().keySet())
			try {
				LOG.trace("{}",  eBean );				
				Object value = eBean.get(properyName);
				//sender.sendMessage("-#"+uid+"#"+updateCounter+"  #"+ properyName +" == '"+ value+"'");
				retval+=suffix;
				retval+="{";	
				retval+=properyName +"=='"+ value+"'";
				retval+="}";
				suffix=",";
			}catch(Exception ex) {
				LOG.trace("{}", ex);
			}
		}	
		retval+="]";
		sender.sendMessage(retval);
	}

	public String toString() {		
		String retval = "DL#"+uid;
		return retval ;
	}
}

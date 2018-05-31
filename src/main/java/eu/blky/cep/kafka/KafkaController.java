package eu.blky.cep.kafka;
 
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.OnClose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.*; 

import com.espertech.esper.client.EPRuntime;
 
@Controller 
@Scope("singleton")
public class KafkaController {
	
	@OnClose
	private void stopAll() {
		kafkaReader.stopMonitoring();
	}

    @Autowired
    private CepKafkaDefaultConsumer kafkaReader;

    @Autowired
    private CepKafkaDefaultProducer kafkaWriter;

	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(KafkaController.class);    

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public @ResponseBody String testMestod(HttpServletRequest request){
       request.getSession().setAttribute("kafkaWriter",kafkaWriter);
       request.getSession().setAttribute("kafkaReader",kafkaReader);
       return "inited";
    }    
     
    /**
    * Rest web service
    */
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public @ResponseBody  boolean postToKafka(String message) {
    	LOG.debug("/send:@ [{}] :: {} / {} ", message, kafkaReader, kafkaWriter);
    	try {
			kafkaWriter.send(message);
			return true;
		} catch (InterruptedException e) {
			LOG.error("InterruptedException ",e);
		} catch (ExecutionException e) {
			LOG.error("InterruptedException ",e);
		}
        return false;
    }
    
    
    /**
    * Rest web service
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody List<KafkaSession>  list(String message) {
    	List<KafkaSession> retval = new ArrayList<>();
    	for (EPRuntime next: kafkaReader.getListeners()) {
    		KafkaSession e = new KafkaSession(""+next);    		
			retval.add(e );
    	}
		return  retval ; 
    }
}
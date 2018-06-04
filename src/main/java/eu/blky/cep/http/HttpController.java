package eu.blky.cep.http;
 
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
 
import org.springframework.web.bind.annotation.*;
import eu.blky.cep.weso.ace4cep.CepKeeper; 
 
@Controller 
@Scope("session")
public class HttpController {
    public static final String CEP_KEEPER = "cepKeeper";

	@Resource(name = CEP_KEEPER) 
    CepKeeper cepKeeper;
    
	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(HttpController.class);    

	/**
	 * 
	 * this method should be called together with first Web-Client (for ex. <body onload="...">) 
	 * 
	 * @param request
	 * @return
	 */
    @RequestMapping(value = "/initcep", method = RequestMethod.GET)
    public @ResponseBody String testMestod(HttpServletRequest request){
       request.getSession().setAttribute(CEP_KEEPER,cepKeeper);       
       return "inited";
    }   	
    // SpringWEb v.4.x.x
    @RequestMapping(value = "/push/{id}", method = RequestMethod.GET)  
    public @ResponseBody String push2cep(@PathVariable("id") long id) {
    	LOG.info("public String push2cep(@PathVariable(\"id\") long id) {"+id);
    	try {
            Object object = new HttpPushObject(id);
    		cepKeeper.getCepRT().sendEvent(object);
    		
    	}catch(Exception e) {
    		LOG.error("  public String push2cep(@PathVariable(\"id\") long id) {}", e);
    	}
        return ""+id;
    }
    // SpringWEb v.4.x.x
    @RequestMapping(value = "/push/{key}/value/{value}", method = RequestMethod.GET)  
    public @ResponseBody String push2cep(@PathVariable("key") long key,@PathVariable("value") double value) {
    	LOG.info("public String push2cep({},{})) ", key , value);
    	try {
            Object object = new HttpPushObject(key,value);
    		cepKeeper.getCepRT().sendEvent(object);
    		
    	}catch(Exception e) {
    		LOG.error("  public String push2cep(@PathVariable(\"id\") long id) {}", e);
    	}
        return ""+key+"="+value;
    }    
     
	@PreDestroy
	private void cleanUp() {
		LOG.debug("destroy...");
		cepKeeper.destroy();
		LOG.debug("..is done.");
	}
    
 
}
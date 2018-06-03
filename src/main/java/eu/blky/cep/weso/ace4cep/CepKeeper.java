package eu.blky.cep.weso.ace4cep;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;

 

@Component
@Scope("session") 
public class CepKeeper {

    private EPServiceProvider cep;
	public EPServiceProvider getCep() {
		return cep;
	}

	public void setCep(EPServiceProvider cep) {
		this.cep = cep;
	}	
 
	private EPAdministrator cepAdm;
	// The Configuration is meant only as an initialization-time object.
	// @Autowired
	private Configuration cepConfig = new Configuration(); // com.espertech.esper.client;//= new Configuration();

	private EPRuntime cepRT;  
	
	public EPAdministrator getCepAdm() {
		return cepAdm;
	}

	public void setCepAdm(EPAdministrator cepAdm) {
		this.cepAdm = cepAdm;
	}

	public Configuration getCepConfig() {
		return cepConfig;
	}

	public EPRuntime getCepRT() {
		return cepRT;
	}

	public void setCepRT(EPRuntime cepRT) {
		this.cepRT = cepRT;
	}

	public void destroy() {
		getCepAdm().destroyAllStatements();
		getCep().destroy();		
	}	
}

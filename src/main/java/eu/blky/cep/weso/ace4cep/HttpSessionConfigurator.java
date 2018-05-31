package eu.blky.cep.weso.ace4cep;

 
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
    	try {
    		System.out.println("modifyHandshake() Current thread " + Thread.currentThread().getName());
    		
    		sec.getUserProperties().put("http_session", request.getHttpSession());
    		
    		String user = request.getParameterMap().get("user").get(0);
    		sec.getUserProperties().put(user, request.getHttpSession());
    		System.out.println("modifyHandshake() User " + user + " with http session ID " + ((HttpSession) request.getHttpSession()).getId());
    	}catch(Throwable e) {
    		e.printStackTrace();
    		super.modifyHandshake(sec, request, response);
    	}	
    }

}
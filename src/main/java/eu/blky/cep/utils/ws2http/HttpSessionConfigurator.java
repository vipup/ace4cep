package eu.blky.cep.utils.ws2http;

 
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    public static final String HTTP_SESSION = "http_session";
	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(HttpSessionConfigurator.class);

	@Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
    	try {
    		LOG.debug( "modifyHandshake() Current thread :{}" , Thread.currentThread() );
    		
    		sec.getUserProperties().put(HTTP_SESSION, request.getHttpSession());
    		String user = request.getParameterMap().get("user").get(0);
    		sec.getUserProperties().put(user, request.getHttpSession());
    		LOG.debug("modifyHandshake() User {} with http session ID :{}", user  ,  ((HttpSession) request.getHttpSession()).getId());
    	}catch(Throwable e) {
    		LOG.error( "public void modifyHandshake::Throwable ", e );
    		super.modifyHandshake(sec, request, response);
    	}	
    }

}
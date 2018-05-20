package eu.blky.cep.weso.ace4cep;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
  
 
@ServerEndpoint(value = "/websocket/chat")
public class ChatAnnotation {

    private static final Log LOG = LogFactory.getLog(ChatAnnotation.class);

    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<ChatAnnotation> connections =
            new CopyOnWriteArraySet<ChatAnnotation>();

    private String nickname;
    private Session session;

    public ChatAnnotation() {
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
    }


    @OnOpen
    public void start(Session session) {
        this.session = session;
        nickname = session.getUserPrincipal().getName()+"#"+session.getId();
        connections.add(this);
        String message = String.format("* %s %s", nickname, "has joined.");
        broadcast(message);
    }


    @OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("* %s %s", nickname, "has disconnected.");
        broadcast(message);
    }
    
    @OnMessage
    public void incoming(String message) {
    	//
    	if ("who".equals(message)) {
    		String listOfCepStatements ="ANONYMOUS";
    		try {
    			for(ChatAnnotation con:connections) {
    				listOfCepStatements += ",";
    				listOfCepStatements += con.nickname;
    			}
    			session.getBasicRemote().sendText("connected: "+ listOfCepStatements);
    		}catch(IOException e) {
    			// Ignore
    		}
    	} 
    	 
    	else {
	        // Never trust the client
	        String filteredMessage = String.format("%s: %s",   nickname, HTMLFilter_filter(message.toString()));
	        broadcast(filteredMessage);
    	}
    }
 



    private Object HTMLFilter_filter(String message) {
        if (message == null)
            return (null);

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
            case '<':
                result.append("&lt;");
                break;
            case '>':
                result.append("&gt;");
                break;
            case '&':
                result.append("&amp;");
                break;
            case '"':
                result.append("&quot;");
                break;
            default:
                result.append(content[i]);
            }
        }
        String retval = (result.toString());
        return retval;

	}


	@OnError
    public void onError(Throwable t) throws Throwable {
		LOG.error("Chat Error: " + t.toString(), t);
    }


    private static void broadcast(String msg) {
        for (ChatAnnotation client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
            	LOG.fatal("Chat Error: Failed to send message to client", e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                	LOG.error("client.session.close();", e1);
                	System.exit(-222);
                }
                String message = String.format("* %s %s",
                        client.nickname, "has been disconnected.");
                broadcast(message);
            }
        }
    }
}





    


package eu.blky.cep.weso.ace4cep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.espertech.esper.client.UpdateListener;
 
 
  
 
@ServerEndpoint(value = "/websocket/chat")
public class ChatAnnotation {

    private static final Log LOG = LogFactory.getLog(ChatAnnotation.class);

    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<ChatAnnotation> connections =
            new CopyOnWriteArraySet<ChatAnnotation>();

	private static final String CEP_KEEPER = "CEP_KEEPER";

    private String nickname;
    private Session session;

	private int engineCounter;

	private List<Messenger> activeMessengers =  new ArrayList<Messenger>();

    public ChatAnnotation() {
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
    }


    @OnOpen
    public void start(Session session) {
        this.session = session;
        nickname = session.getUserPrincipal().getName()+"#"+session.getId();
        initSession();
        connections.add(this);
        String message = String.format("* %s %s", nickname, "has joined.");
        broadcast(message);
    }

    private void initSession() {
    	Map<String, Object> props = this.session.getUserProperties();
		CepKeeper newKeeper = new CepKeeper();
		Configuration cepConfig = new com.espertech.esper.client.Configuration();

		props.put(CEP_KEEPER, newKeeper );
		// <bean name="cepConfiguration" class="com.espertech.esper.client.Configuration" />
		newKeeper .setCepConfig(cepConfig );
		initCEP();
	}
    
	private EPRuntime initCEP(){  
		//getKeeper().getCepConfig().addEventType("OrderTick", OrderTick.class.getName()); 
		CepKeeper keeper = getKeeper();
		Configuration cepConfig = keeper.getCepConfig();
		EPServiceProvider provider = EPServiceProviderManager.getProvider("myCEPEngine#"+engineCounter++, cepConfig);
		keeper.setCep(provider);
		EPServiceProvider cep = keeper.getCep();
		EPRuntime epRuntime = cep.getEPRuntime();
		keeper.setCepRT(epRuntime); 
		EPAdministrator epAdministrator = cep.getEPAdministrator();
		keeper.setCepAdm(epAdministrator);   
	    
//	    // step 4: summaryze that all
//	    String eql4 = "insert into TicksPerSecond \n" + 
//	    		"select  'PoloTick' type,  pair, count(*) as cnt \n" + 
//	    		"from OrderTick.win:time_batch(11 second) \n" + 
//	    		"group by pair";
//	    EPStatement statStmtTmp = getKeeper().getCepAdm().createEPL(eql4); 
//	    statStmtTmp.addListener(new Statistic2RddUpdater("TicksPerSecond")); 
//	    
//	    // step 5: summaryze that all
//	    String eql5 = "" + 
//	    		"select   type , sum(  cnt  )" + 
//	    		"from TicksPerSecond.win:time_batch(1 second) " + 
//	    		"group by type";
//	    EPStatement statByTypeTmp = getKeeper().getCepAdm().createEPL(eql5); 
//	    statByTypeTmp.addListener(new StatisticPrinter());	    
	    
	    return keeper.getCepRT();
	
	}    

	@OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("* %s %s", nickname, "has disconnected.");
        broadcast(message);
    }
 
	private EPStatement createEPStatement(String eql3) {
		return getCepAdm().createEPL(eql3);
	}
	private EPAdministrator getCepAdm() {
		return getKeeper().getCepAdm();
	}
	
	
    private CepKeeper getKeeper() { 
		return (CepKeeper) session.getUserProperties().get(CEP_KEEPER  );
	}


	@OnMessage
    public void incoming(String message) {

		
    	if ("who".equals(message)) { // list of registered sessions
   		 	exec_who(message);
    	}else if ("help".equals(message)) { 
    		responce("available commands: help, who, stopall, startall, killall, hide{all}");
    	}else if ("ss".equals(message)) { 
    		exec_ss(message);
    	}else if ("hideall".equals(message)) {  
    		exec_hide(message);
    	}else if ("hide".equals(message)) { 
    		exec_hide(message);
    	}else if ("stopall".equals(message)) {  
    		exec_stopall(message);
    	}else if ("startall".equals(message)) {  
    		exec_startall(message);
    	}else if ("killall".equals(message)) { 
    		exec_killall(message);
    	}else  try { // toExec. assumes  - input is cep-command
    		for (String eqlTmp : cleanUpMessages(message)) {
    		    		EPStatement priceSTMT = createEPStatement(eqlTmp); 
    		    		Messenger proxyTmp = getMessanger(priceSTMT.getName());
    		    		UpdateListener ulTmp = new Defaulistener(proxyTmp);
    		    		priceSTMT.addListener(ulTmp );    
    		    		responce(ulTmp.toString());
    		}
    	}catch (Exception e) {
			// TODO: handle any  exception - just send the message back... 
    	    // && Never trust the client
	        String filteredMessage = String.format("%s: %s", nickname, HTMLFilter_filter(message.toString()));
	        responce(filteredMessage);
	        responce("ERROR! "+e.getMessage());
	        
    	}		
    } 
	
	private ArrayList<String> cleanUpMessages(String message) {
		ArrayList<String> retval = new ArrayList<String>();
		String nextEQL="";
		for (String line:message.split("\n")) {
			line = line.trim();
			if (line.startsWith("--"))continue;
			if (line.endsWith(";")) {
				line = line.substring(0, line.length()-1); 
				nextEQL += line + " ";
				retval.add(nextEQL);
				nextEQL="";
			}else {
				nextEQL += line + " ";
			}
			nextEQL += " ";
		}
		retval.add(nextEQL);
		return retval;
	}


	private Messenger getMessanger(String statementName) {
		Messenger messenger = new Messenger() { 
			private boolean enabled = true;

			@Override
			public void sendMessage(String string) {
				if (enabled)
				try {
					session.getBasicRemote().sendText("."+statementName+"."+string);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}

			@Override
			public void hide() {
				this.enabled  = false;
				
			}};
		activeMessengers.add(messenger);	
		return messenger;		
	}


	private String map2string(Map  vars) {
		String retval = "";
		Set<String> keys = vars.keySet();
		for (String key:keys ) {
			retval += key +" ==: [" + vars.get(key) +"]," ;
		}
		return retval;
	}

	private HashMap<String, CepPair>  listAllListeners(String[] StatementNames) {
		HashMap<String, CepPair> retval = new HashMap<>(); 
		for(String name:StatementNames) {			
			EPStatement stmtTmp =  getKeeper().getCepAdm().getStatement(name);
			CepPair nextTmp = new CepPair(name, stmtTmp);
			retval.put(name, nextTmp);
		}
		return retval;
	} 

	//exec_hideactive
	private void exec_hide(String message) {
		for (Messenger m:this.activeMessengers) {
			m.hide();			
		}
		activeMessengers.clear();
		
	}
	//exec_rmall
	private void exec_startall(String message) {
		CepKeeper cepKeeper = getKeeper();
		cepKeeper.getCepAdm().startAllStatements();
		exec_ss(message);
	}

	
	private void exec_stopall(String message) {
		CepKeeper cepKeeper = getKeeper();
		cepKeeper.getCepAdm().stopAllStatements();
		exec_ss(message);
	}

	private void exec_killall(String message) {
		CepKeeper cepKeeper = getKeeper();
		cepKeeper.getCepAdm().destroyAllStatements();
		exec_ss(message);
	}
	
	private void exec_ss(String message) {
		CepKeeper cepKeeper = getKeeper(); 
		String[] StatementNames = cepKeeper.getCepAdm().getStatementNames(); 
		HashMap<String, CepPair> listeners = listAllListeners(StatementNames);
		responce(map2string(listeners )); 
	}
 

	private void responce(String filteredMessage) {
		try {
			session.getBasicRemote().sendText(filteredMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void exec_who(String msg) {
		String listOfCepStatements = "ANONYMOUS";
		try {
			for (ChatAnnotation con : connections) {
				listOfCepStatements += ",";
				listOfCepStatements += con.nickname;
			}
			session.getBasicRemote().sendText("connected: " + listOfCepStatements);
		} catch (IOException e) {
			// Ignore
		}
	};
 
 



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





    


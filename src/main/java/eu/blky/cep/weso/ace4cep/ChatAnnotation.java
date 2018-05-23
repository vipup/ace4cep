package eu.blky.cep.weso.ace4cep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.LinkedHashMap;
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
import com.espertech.esper.client.ConfigurationOperations;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement; 
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.mycompany.MyClass;
import com.mycompany.MyKafkaEvent;
import com.mycompany.Sensor;
 
 
  
 
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

    private void destroySession() {
    	hookToKill.stopMonitoring();
    	kafkaHook.stopMonitoring();
    	mySensor.stop();
    	
    	Map<String, Object> props = this.session.getUserProperties();
    	getKeeper().destroy();
    	props.remove(CEP_KEEPER);
    }
    private void initSession() {
    	Map<String, Object> props = this.session.getUserProperties();
		CepKeeper newKeeper = new CepKeeper();
		Configuration cepConfig = new Configuration(); // com.espertech.esper.client

		props.put(CEP_KEEPER, newKeeper );
		// <bean name="cepConfiguration" class="com.espertech.esper.client.Configuration" />
		newKeeper .setCepConfig(cepConfig );
		initCEP();
	}
	MyKafkaEvent kafkaHook = new  MyKafkaEvent(null);
	private EPRuntime initCEP(){  
		//getKeeper().getCepConfig().addEventType("OrderTick", OrderTick.class.getName()); 
		// CFG-> SP -> RT 
		//       +---> ADM
		CepKeeper keeper = getKeeper();
		Configuration cepConfig = keeper.getCepConfig();
		EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine#"+engineCounter++, cepConfig);
		/* SET */	keeper.setCep(cep);
		EPRuntime epRuntime = cep.getEPRuntime(); 		
		/* SET */	keeper.setCepRT(epRuntime); //cepConfig.get
		EPAdministrator epAdministrator = cep.getEPAdministrator();
		/* SET */	keeper.setCepAdm(epAdministrator);
		

        ConfigurationOperations configurationTmp = epAdministrator.getConfiguration();
		// Fake EventType definition  --------------------- Sensor
        setupMySensor(epRuntime, configurationTmp );	 	
		
		// Fake EvenrType def --------------- MyEvent.somefield
		setupMyEvent(epRuntime, configurationTmp);
		
		// http://esper.espertech.com/release-7.1.0/esper-reference/html/extension.html#extension-virtualdw
		// 17.3.2. Configuring the Single-Row Function Name
		setupMyClass(configurationTmp);

		//setupKafkaInput(cepConfig);
		//setupKafkaOutput(cepConfig);
		
		setupMyKafkaEvent(epRuntime, configurationTmp);
		
	    return keeper.getCepRT();
	}


	private void setupMyKafkaEvent(EPRuntime epRuntime, ConfigurationOperations configurationTmp) {
		configurationTmp.addEventType("MyKafkaEvent", com.mycompany.MyKafkaEvent.class);
		kafkaHook.startMonitoring(epRuntime);

	}

 

	private ConfigurationOperations setupMySensor(EPRuntime epRuntime, ConfigurationOperations configurationTmp ) {
		Map<String, Object> definition = new LinkedHashMap<String, Object>();
        definition.put("sensor", String.class);
        definition.put("temperature", double.class);
        
		configurationTmp.addEventType(Sensor.SENSOR_EVENT, definition);
		mySensor = Sensor.getInstance();
		mySensor.startMonitoring(epRuntime);
		return configurationTmp;
	}


	private void setupMyClass(ConfigurationOperations configurationTmp) {
		String className = MyClass.class.getName();
		configurationTmp.addPlugInSingleRowFunction( "myFunction", className, "myFunction");
		configurationTmp.addPlugInSingleRowFunction( "doCompute", className, "doCompute");
		configurationTmp.addPlugInSingleRowFunction( "doCheck", className, "doCheck");
		configurationTmp.addPlugInSingleRowFunction( "doSearch", className, "doSearch");
		configurationTmp.addPlugInSingleRowFunction( "percent", className, "percent");
	}


	private void setupMyEvent(EPRuntime epRuntime, ConfigurationOperations configurationTmp) {
		configurationTmp.addEventType("MyEvent", com.mycompany.MyEvent.class);
		hookToKill = new com.mycompany.MyEvent(111111);
		hookToKill.startMonitoring(epRuntime);
	}    
	com.mycompany.MyEvent hookToKill ;
	Sensor mySensor ;

	@OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("* %s %s", nickname, "has disconnected.");
        broadcast(message);
        destroySession();
    }
 
	private EPStatement createEPStatement(String eql3) {
		return getCepAdm().createEPL(eql3);
	}
	private EPAdministrator getCepAdm() {
		return getKeeper().getCepAdm();
	}
	
	
    private CepKeeper getKeeper() { 
		return (CepKeeper) session.getUserProperties().get(CEP_KEEPER);
	}


	@OnMessage
    public void incoming(String message) {		
    	if ("who".equals(message)) { // list of registered sessions
   		 	exec_who(message);
    	}else if ("help".equals(message)) { 
    		responce("available commands: help, who, stopall, startall, killall, hide{all}");
    		responce("for DataFlow :: sdf:-showDataFlow(s),  i:-instantiate, r:-run, s:-start, c:-cancel, j:-join ");
//EPDataFlowInstance instance = epService.getEPRuntime().getDataFlowRuntime()    		
    	}else if ("sdf".equals(message)) { // show DataFlows
    		String[] dfTmp = this.getKeeper().getCepRT().getDataFlowRuntime().getDataFlows();
    		responce(CepPair.array2string( dfTmp )); 
    	}else if ((""+message).startsWith("i:" )) { // show DataFlows
    		String dataFlowName = message.substring("i:".length() );
			EPDataFlowInstance retval = this.getKeeper().getCepRT().getDataFlowRuntime().instantiate(dataFlowName );
			// TODO :((( 100% CPU @  --MyDataFlow-20.3.7. Select ----------------------  retval.start();
    		responce(  "EPDataFlowInstance =:"+retval 
    				+ " ::: "+ map2string( retval.getParameters() )); 
    	}else if ((""+message).startsWith("r:" )) { // TODO
    		responce("Not implemented yet...");
    	}else if ((""+message).startsWith("s:" )) { // TODO
    		responce("Not implemented yet...");
    	}else if ((""+message).startsWith("c:" )) { // TODO
    		responce("Not implemented yet...");
    	}else if ((""+message).startsWith("j:" )) { // TODO
    		responce("Not implemented yet...");
    	}else if ((""+message).startsWith("r:" )) { // TODO
    		responce("Not implemented yet...");
    	}else if ((""+message).startsWith("r:" )) { // TODO
    		responce("Not implemented yet...");
    	}else if ("ss".equals(message)) { // show statements
    		exec_ss(message);
    	}else if ("se".equals(message)) { // show eventtypes
    		exec_se(message);
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
			// ignore comments
			if (line.startsWith("--"))continue;
			if (line.startsWith("//"))continue;
			if (line.endsWith(";")) {
				line = line.substring(0, line.length()-1); 
				nextEQL += line + "\n ";
				retval.add(nextEQL);
				nextEQL="";
			}else {
				nextEQL += line + "\n ";
			}
			nextEQL += " ";
		}
		if (!"".equals(nextEQL.trim()))retval.add(nextEQL);
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
		try {
			Set<String> keys = vars.keySet();
			
			for (String key:keys ) {
				retval += key +" ==: [" + vars.get(key) +"]," ;
			}
		}catch(Throwable e) {}
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

	private HashMap<String, CepPair>  listEventSenders(String[] StatementNames) {
		HashMap<String, CepPair> retval = new HashMap<>(); 
		for(String name:StatementNames) {			
			EPStatement stmtTmp =  getKeeper().getCepAdm().getStatement(name);
			getKeeper().getCepRT().getEventSender("XX");
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
	
	private void exec_se(String message) {
		CepKeeper cepKeeper = getKeeper(); 
		Configuration cfg = cepKeeper .getCepConfig();
		Map<String, String> etn = cfg .getEventTypeNames();		
		responce(map2string(etn)); 
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





    


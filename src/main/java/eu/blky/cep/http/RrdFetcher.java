package eu.blky.cep.http;
 
import java.io.IOException; 
import java.util.Random;

import org.codehaus.jackson.JsonNode;

public class RrdFetcher {
// TODO	
	public static double fetch(String host, long port, String url ) {
		return new Random().nextDouble();
	}
	public static double fetch(String host, String url ) throws IOException {
		String proto = "http://";
		int port = 8080; 
        String rrdPathTmp = proto+host+port+url;  
        rrdPathTmp = "http://localhost:8080/rrdsaas/fetch.json";
		JsonNode rTmp = JsonReader.readJsonFromUrl(rrdPathTmp);
		//
		double retval = rTmp.get(0).get("data").get(0).get(1).getDoubleValue();
		return retval ;
	}
	public static double fetch(String host, String url , String rrdname) {
		return new Random().nextDouble();
	}

}

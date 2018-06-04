package eu.blky.cep.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonReader {

	public static JsonNode readJsonFromUrl(String url) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
		InputStream is = new URL(url).openStream();
		JsonNode rootNode = objectMapper.readTree(is);
		return rootNode;
	}
}
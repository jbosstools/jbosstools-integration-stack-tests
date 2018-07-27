package org.jboss.tools.drools.ui.bot.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

import org.jboss.tools.drools.reddeer.kienavigator.structure.Project;
import org.jboss.tools.drools.reddeer.kienavigator.structure.Space;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClient {

	private static final String USER_PASS = "testadmin:admin1234;";

	private static final int HTTP_OK = 200;

	private static final String APP_URL = "http://localhost:8080/business-central/";

	private static final String SPACES_CALL = APP_URL + "rest/spaces/";

	private static final String PROJECTS_CALL_EXTENSION = "/projects";

	private static HttpURLConnection getConnection(String url) throws MalformedURLException, IOException {
		HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
		huc.setRequestMethod("GET");
		String basicAuth = "Basic " + new String(DatatypeConverter.printBase64Binary(USER_PASS.getBytes()));
		huc.setRequestProperty("Authorization", basicAuth);
		huc.connect();
		return huc;
	}

	public static int getResponseCode() throws MalformedURLException, IOException {
		return getConnection(APP_URL).getResponseCode();
	}

	private static String getResponse(String url) throws MalformedURLException, IOException {
		StringBuffer response = new StringBuffer();
		HttpURLConnection huc = getConnection(url);

		if (huc.getResponseCode() != HTTP_OK) {
			throw new RuntimeException("Connection problem http response: " + huc.getResponseCode());
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(huc.getInputStream()));
		String line = null;
		while ((line = in.readLine()) != null) {
			response.append(line);
		}
		in.close();
		
		return response.toString();
	}

	public static Space getSpace(String name) throws MalformedURLException, IOException {
		String jsonResponce = getResponse(SPACES_CALL + name);
		return new ObjectMapper().readValue(jsonResponce, Space.class);
	}
	
	public static Project[] getProjects(final String spaceName) throws MalformedURLException, IOException {
		String jsonResponce = getResponse(SPACES_CALL + spaceName + PROJECTS_CALL_EXTENSION);
		return new ObjectMapper().readValue(jsonResponce, Project[].class);
	}
}

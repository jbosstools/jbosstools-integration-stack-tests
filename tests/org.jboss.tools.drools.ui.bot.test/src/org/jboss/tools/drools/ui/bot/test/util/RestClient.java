package org.jboss.tools.drools.ui.bot.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.jboss.tools.drools.reddeer.kienavigator.structure.OrganizationalUnit;
import org.jboss.tools.drools.reddeer.kienavigator.structure.Project;
import org.jboss.tools.drools.reddeer.kienavigator.structure.Repository;

public class RestClient {
	
	private static final String USER_PASS = "testadmin:admin1234;";
	
	private static final int HTTP_OK = 200;
	
	private static final String APP_URL = "http://localhost:8080/business-central/";
	
	private static final String ORG_UNIT_CALL = APP_URL + "rest/organizationalunits/";
	
	private static final String REPOSITORY_CALL = APP_URL + "rest/repositories/";
	
	private static final String PROJECTS_CALL_EXTENSION = "/projects";

	private static HttpURLConnection getConnection(String url) throws MalformedURLException, IOException {
		HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection(); 
		huc.setRequestMethod("GET");
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(USER_PASS.getBytes()));
		huc.setRequestProperty("Authorization", basicAuth);
		huc.connect();
		return huc;
	}
	
	public static int getResponseCode() throws MalformedURLException, IOException {
		return getConnection(APP_URL).getResponseCode();
	}
	
	public static String[] getResponse(String url) throws MalformedURLException, IOException {
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
		return response.toString().replaceAll("[\\{\\}\"\\[\\]]", "").split(",");
	}

	public static OrganizationalUnit getOrganizationalUnit(String name) throws MalformedURLException, IOException {
		return new OrganizationalUnit(getResponse(ORG_UNIT_CALL + name));
	}
	
	public static Repository getRepository(String name) throws MalformedURLException, IOException {
		return new Repository(getResponse(REPOSITORY_CALL + name));
	}
	
	public static List<Project> getProjects(String repositoryName) throws MalformedURLException, IOException {
		List<Project> projects = new ArrayList<Project>();
		String[] response = getResponse(REPOSITORY_CALL + repositoryName + PROJECTS_CALL_EXTENSION);
		
		for (int i = 0; i < response.length; i += 4) {
			Project p = new Project(Arrays.copyOfRange(response, i, i + 4));
			projects.add(p);
		}
		return projects;
	}
}

package org.jboss.tools.teiid.reddeer.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.util.Base64;

/**
 * A very simple implemetation of a http client before I figure out how to include Apache HttpClient dependency
 * 
 * @author asmigala
 *
 */
public class SimpleHttpClient {

	private final String url;
	private String credentials;
	private Map<String, String> headers = new HashMap<String, String>();

	public SimpleHttpClient(String url) {
		this.url = url;
	}

	public SimpleHttpClient setBasicAuth(String username, String password) {
		credentials = username + ':' + password;
		return this;
	}

	public SimpleHttpClient addHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}

	public String get() throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(this.url).openConnection();
		connection.setRequestMethod("GET");
		addBasicHeader(connection);
		addHeaders(connection);
		connection.connect();
		return readResponse(connection);
	}

	public String post(String data) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(this.url).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		addBasicHeader(connection);
		addHeaders(connection);
		connection.connect();
		writeRequestData(connection, data);
		return readResponse(connection);	
	}

	private void writeRequestData(HttpURLConnection connection, String data) throws IOException {
		OutputStream outputStream = null;
		try {
			outputStream = connection.getOutputStream();
			outputStream.write(data.getBytes());
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void addHeaders(HttpURLConnection connection) {
		for (Entry<String, String> e : headers.entrySet()) {
			connection.addRequestProperty(e.getKey(), e.getValue());
		}

	}

	private void addBasicHeader(HttpURLConnection connection) {
		if (credentials != null) {
			String encoded = Base64.encodeBytes(credentials.getBytes());
			connection.setRequestProperty("Authorization", "Basic " + encoded);
		}
	}

	private String readResponse(HttpURLConnection connection) throws IOException {
		InputStream response = null;
		try {
			response = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(response));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} finally {
			try {
				response.close();
			} catch (NullPointerException e) {
				// ignore - connection is null
			}
		}
	}
	
	/**
	 * Creates and posts specified SOAP request using teiidUser credentials and HTTP-Basic security.
	 * @return server response
	 * @throws IOException if request fails
	 */
	public static String postSoapRequest(TeiidServerRequirement teiidServer, String url, String soapAction, String request) throws IOException{
		String username = teiidServer.getServerConfig().getServerBase().getProperty("teiidUser");
		String password = teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword");
		System.out.println("Using HTTPBasic security with username '" + username + "' and password '" + password + "'");
		return new SimpleHttpClient(url)
				.setBasicAuth(username, password)
				.addHeader("Content-Type", "text/xml; charset=utf-8")
				.addHeader("SOAPAction", soapAction)
				.post(request);
	}
	
	/**
	 * Creates and posts specified SOAP request using None security.
	 * @return server response
	 * @throws IOException if request fails
	 */
	public static String postSoapRequest(String url, String soapAction, String request) throws IOException{
		return new SimpleHttpClient(url)
				.addHeader("Content-Type", "text/xml; charset=utf-8")
				.addHeader("SOAPAction", soapAction)
				.post(request);
	}

}
package org.jboss.tools.switchyard.ui.bot.test.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Simple REST Service
 * 
 * @author apodhrad
 * 
 */
@SuppressWarnings("restriction")
public class RESTService {

	public static final int DEFAULT_PORT = 8080;
	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final String DEFAULT_CONTEXT = "/rest";

	private int port;
	private String host;
	private HttpServer server;

	public RESTService() {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public RESTService(int port) {
		this(DEFAULT_HOST, port);
	}

	public RESTService(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() {
		start(DEFAULT_CONTEXT);
	}

	public void start(final String context) {
		try {
			server = HttpServer.create(new InetSocketAddress(InetAddress.getByName(host), port), 0);
			server.createContext(context, new HttpHandler() {

				public void handle(HttpExchange exchange) throws IOException {
					String path = exchange.getRequestURI().getPath();
					info("Received path '" + path + "'");
					String name = path.substring(path.indexOf(context) + context.length()).replaceAll("/", "");
					String response = "Hello " + name;
					info("Response set to '" + response + "'");
					exchange.sendResponseHeaders(200, response.getBytes().length);
					OutputStream os = exchange.getResponseBody();
					os.write(response.getBytes());
					os.close();
				}

			});
			server.setExecutor(null);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("IOException");
		}
		info("REST service started at http://" + host + ":" + port + context);
	}

	public void stop() {
		server.stop(0);
		info("REST service stopped");
	}

	protected void info(String msg) {
		System.out.println(new Date() + "\tINFO\t" + msg);
	}
}

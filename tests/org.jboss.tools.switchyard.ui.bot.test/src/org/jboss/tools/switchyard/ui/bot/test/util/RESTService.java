package org.jboss.tools.switchyard.ui.bot.test.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * Simple REST Service
 * 
 * @author apodhrad
 * 
 */
public class RESTService {

	public static final int DEFAULT_PORT = 8080;
	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final String DEFAULT_CONTEXT = "/rest";

	private int port;
	private String host;
	private Server server;

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
			InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(host), port);
			Server server = new Server(address);
			server.setHandler(new AbstractHandler() {

				@Override
				public void handle(String data, Request baseRequest, HttpServletRequest request,
						HttpServletResponse response) throws IOException, ServletException {
					info("Data: " + data);
					String message = "Unknown request '" + data + "'";
					if (data.contains(context)) {
						message = "Hello " + data.substring(context.length()).replaceAll("/", "");
					}
					PrintWriter out = response.getWriter();
					out.println(message);
					out.flush();
					out.close();
				}
			});
			server.start();
			info("REST service started at http://" + host + ":" + port + context);
		} catch (Exception e) {
			e.printStackTrace();
			info(e.getMessage());
		}
	}

	public void stop() {
		try {
			server.stop();
			info("REST service stopped");
		} catch (Exception e) {
			e.printStackTrace();
			info(e.getMessage());
		}
	}

	protected static void info(String msg) {
		System.out.println(new Date() + "\tINFO\t" + msg);
	}
	
	public static void main(String[] args) {
		new RESTService(8123).start();
	}
}

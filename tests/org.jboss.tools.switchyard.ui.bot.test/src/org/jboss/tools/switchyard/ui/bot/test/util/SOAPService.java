package org.jboss.tools.switchyard.ui.bot.test.util;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

public class SOAPService {

	public static final int DEFAULT_PORT = 8080;
	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final String DEFAULT_CONTEXT = "/soap";

	private int port;
	private String host;
	private Endpoint endPoint;

	public SOAPService() {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public SOAPService(int port) {
		this(DEFAULT_HOST, port);
	}

	public SOAPService(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() {
		start(DEFAULT_CONTEXT);
	}

	public void start(final String context) {
		endPoint = Endpoint.create(new HelloService());
		endPoint.publish("http://" + host + ":" + port + context);
		info("SOAP service started at http://" + host + ":" + port + context);
	}

	public void stop() {
		endPoint.stop();
		info("SOAP service stopped");
	}

	@WebService
	public class HelloService {

		@WebMethod
		public String sayHello(@WebParam(name = "name") String name) {
			return "Hello " + name;
		}
	}

	protected void info(String msg) {
		System.out.println(new Date() + "\tINFO\t" + msg);
	}
}

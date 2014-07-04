package org.jboss.tools.fuse.ui.bot.test.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.junit.requirement.PropertyConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.tools.fuse.ui.bot.test.requirement.ServerRequirement.Server;

/**
 * Server requirement using configuration from XML file
 * 
 * @author tsedmik
 */
public class ServerRequirement implements Requirement<Server>, PropertyConfiguration{

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Server {

	}
	
	// Server runtime environments
	private String runtime; // "Select the type of runtime environment:"
	private String path; // "Installation directory:"
	
	// New Server Wizard
	private String type; // from the tree "Select the server type:"
	private String hostname; // "Server's host name:"
	private String name; // "Server name:"
	private String port;
	private String username;
	private String password;
	
	private String jmxname; // name of the server process in JMX Navigator

	@Override
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {
		System.out.println("Fulfilling Server requirement with\nName: " + type + "\nPath: " + path);
	}

	@Override
	public void setDeclaration(Server declaration) {
		
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJmxname() {
		return jmxname;
	}

	public void setJmxname(String jmxname) {
		this.jmxname = jmxname;
	}
}

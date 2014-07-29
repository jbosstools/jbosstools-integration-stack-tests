package org.jboss.tools.runtime.reddeer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.Activator;
import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.preference.Preferences;
import org.jboss.tools.runtime.reddeer.wizard.ServerRuntimeWizard;
import org.jboss.tools.runtime.reddeer.wizard.ServerWizard;

/**
 * Apache Karaf Server
 * 
 * @author apodhrad
 * 
 */
@XmlRootElement(name = "karaf", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerKaraf extends ServerBase {

	private final String category = "JBoss Fuse";

	private final String label = "Apache Karaf";

	@XmlElement(name = "host", namespace = Namespaces.SOA_REQ)
	private String host;
	@XmlElement(name = "port", namespace = Namespaces.SOA_REQ)
	private String port;
	@XmlElement(name = "username", namespace = Namespaces.SOA_REQ)
	private String username;
	@XmlElement(name = "password", namespace = Namespaces.SOA_REQ)
	private String password;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	public String getCategory() {
		return category;
	}

	public String getServerType() {
		return label + " " + getVersion() + " Server";
	}

	public String getRuntimeType() {
		return "Runtime definition for " + label + " " + getVersion();
	}
	
	public String getRuntimeName() {
		return "Runtime definition for " + label + " " + getVersion();
	}

	@Override
	public void create() {
		// Add runtime
		RuntimePreferencePage runtimePreferencePage = new RuntimePreferencePage();
		runtimePreferencePage.open();
		runtimePreferencePage.addRuntime();
		ServerRuntimeWizard runtimeWizard = new ServerRuntimeWizard();
		runtimeWizard.setType(getCategory(), getRuntimeType());
		runtimeWizard.next();
		runtimeWizard.setInstallationDir(getHome());
		runtimeWizard.finish();
		runtimePreferencePage.ok();

		// Add server
		ServerWizard serverWizard = new ServerWizard();
		serverWizard.open();
		serverWizard.setType(getCategory(), getServerType());
		serverWizard.setName(name);
		serverWizard.setRuntime(getRuntimeName());
		serverWizard.next();
		serverWizard.setPort(getPort());
		serverWizard.setUsername(getUsername());
		serverWizard.setPassword(getPassword());
		serverWizard.finish();

		// Set ssh home
		Preferences.set("org.eclipse.jsch.core", "SSH2HOME", Activator.getResources(".ssh").getAbsolutePath());

		// Copy host.key
		File src = Activator.getResources("host.key");
		File dest = new File(getHome(), "etc/host.key");
		try {
			copyFileUsingStream(src, dest);
		} catch (IOException e) {
			throw new RuntimeException("Can't copy 'host.key' file!", e);
		}
	}

	/**
	 * Copies a source file to given destination
	 * 
	 * @param source
	 *            Source file
	 * @param dest
	 *            Destination
	 * @throws IOException
	 */
	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
		}
	}

}

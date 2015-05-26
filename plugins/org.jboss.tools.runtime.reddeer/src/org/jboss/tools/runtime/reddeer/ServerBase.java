package org.jboss.tools.runtime.reddeer;

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.preference.InstalledJREs;

/**
 * 
 * @author apodhrad
 * 
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public abstract class ServerBase extends RuntimeBase {
	
	public static final String DEFAULT_JRE = "default";
	
	private String jre;
	
	private String jreName;

	@XmlElement(name = "jre", namespace = Namespaces.SOA_REQ, defaultValue = DEFAULT_JRE)
	public String getJre() {
		return jre;
	}

	public void setJre(String jre) {
		if (jre.equals(DEFAULT_JRE)) {
			return;
		}
		File jreFile = new File(jre);
		if (!jreFile.exists()) {
			throw new IllegalArgumentException("JRE path '" + jre + "' doesn't exist.");
		}
		this.jre = jre;
		this.jreName = jreFile.getName();
	}
	
	public String getJreName() {
		return jreName;
	}

	public void setState(ServerReqState requiredState) {
		ServersView serversView = new ServersView();
		serversView.open();
		Server server = serversView.getServer(name);

		ServerState currentState = server.getLabel().getState();
		switch (currentState) {
		case STARTED:
			if (requiredState == ServerReqState.STOPPED)
				server.stop();
			break;
		case STOPPED:
			if (requiredState == ServerReqState.RUNNING && canStart()) {
				try {
					server.start();
				} catch (Exception e) {
					try {
						server = new ServersView().getServer(name);
						server.stop();
					} catch (Exception ex) {

					}
					server = new ServersView().getServer(name);
					server.start();
				}
			}
			break;
		default:
			new AssertionError("It was expected to have server in " + ServerState.STARTED + " or "
					+ ServerState.STOPPED + "state." + " Not in state " + currentState + ".");
		}
	}
	
	/**
	 * Returns whether the server can be started.
	 * 
	 * @return whether the server can be started
	 */
	protected boolean canStart() {
		return true;
	}
	
	/**
	 * Adds new jre if it is defined. 
	 */
	protected void addJre() {
		if (jre != null) {
			InstalledJREs installedJREsPage = new InstalledJREs();
			installedJREsPage.open();
			installedJREsPage.addJre(jre, jreName);
			installedJREsPage.ok();
		}
	}

	@Override
	public boolean exists() {
		IServer[] server = ServerCore.getServers();
		for (int i = 0; i < server.length; i++) {
			if (server[i].getId().equals(name)) {
				return true;
			}
		}
		return false;
	}

}

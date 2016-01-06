package org.jboss.tools.runtime.reddeer;

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerLabel;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.runtime.reddeer.preference.InstalledJREs;

/**
 * 
 * @author apodhrad
 * 
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public abstract class ServerBase extends RuntimeBase {

	public static final String ADD_REMOVE_LABEL = "Add and Remove...";
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

	public abstract int getHttpPort();

	public String getRuntimeName() {
		return getName() + " Runtime";
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
			new AssertionError("It was expected to have server in " + ServerState.STARTED + " or " + ServerState.STOPPED
					+ "state." + " Not in state " + currentState + ".");
		}
	}

	public void deployProject(String project) {
		deployProject(project, "Deployed \"" + project + ".jar\"");
	}

	public void deployProject(String project, String checkPhrase) {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		consoleView.clearConsole();

		ServersView serversView = new ServersView();
		serversView.open();
		Tree tree = new DefaultTree();
		for (TreeItem item : tree.getItems()) {
			ServerLabel serverLabel = new ServerLabel(item);
			if (serverLabel.getName().equals(getName())) {
				item.select();
				new ContextMenu(ADD_REMOVE_LABEL).select();
				new DefaultShell(ADD_REMOVE_LABEL);
				new DefaultTreeItem(project).select();
				new PushButton("Add >").click();
				new PushButton("Finish").click();
				new WaitWhile(new ShellWithTextIsActive(ADD_REMOVE_LABEL), TimePeriod.LONG);
				new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
				checkDeployment(project, checkPhrase);
				return;
			}
		}
		throw new RuntimeException("Cannot find server '" + getName() + "'");
	}

	protected void checkDeployment(String project, String checkPhrase) {
		new WaitUntil(new ConsoleHasText(checkPhrase), TimePeriod.LONG);
	}

	public String getUrl(String path) {
		return getUrl("localhost", path);
	}

	public abstract String getUrl(String host, String path);

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

	public boolean isRemote() {
		return false;
	}

}

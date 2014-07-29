package org.jboss.tools.fuse.reddeer.server;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.condition.ServerExists;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerLabel;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.preference.ServerRuntimePreferencePage;
import org.jboss.tools.fuse.reddeer.view.FuseJMXNavigator;
import org.jboss.tools.fuse.reddeer.wizard.ServerWizard;

/**
 * Performs operation with a Fuse server
 * 
 * @author tsedmik
 */
public class ServerManipulator {

	private static final Logger log = Logger.getLogger(ServerManipulator.class);

	public static void addServerRuntime(String type, String path) {

		ServerRuntimePreferencePage serverRuntime = new ServerRuntimePreferencePage();
		serverRuntime.open();
		serverRuntime.addServerRuntime(type, path);
		serverRuntime.ok();

	}

	public static void editServerRuntime(String name, String path) {

		ServerRuntimePreferencePage serverRuntime = new ServerRuntimePreferencePage();
		serverRuntime.open();
		serverRuntime.editServerRuntime(name, path);
		serverRuntime.ok();

	}

	public static void removeServerRuntime(String name) {

		ServerRuntimePreferencePage serverRuntime = new ServerRuntimePreferencePage();
		serverRuntime.open();
		serverRuntime.removeServerRuntime(name);
		serverRuntime.ok();

	}

	public static List<String> getServerRuntimes() {

		ServerRuntimePreferencePage serverRuntime = new ServerRuntimePreferencePage();
		serverRuntime.open();
		List<String> temp = serverRuntime.getServerRuntimes();
		serverRuntime.cancel();
		return temp;
	}

	public static void addServer(String type, String hostname, String name, String portNumber, String userName,
			String password) {

		ServerWizard serverWizard = new ServerWizard();
		serverWizard.setType(type);
		serverWizard.setHostName(hostname);
		serverWizard.setName(name);
		serverWizard.setPortNumber(portNumber);
		serverWizard.setUserName(userName);
		serverWizard.setPassword(password);
		serverWizard.execute();

	}

	public static void removeServer(String name) {

		log.info("Deleting server " + name + ".");
		new ServersView().open();
		new DefaultTreeItem().select();
		new ContextMenu("Delete").select();
		new DefaultShell("Delete Server");
		new PushButton("OK").click();
		new WaitWhile(new ServerExists(name), TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		log.info("Server " + name + " was deleted.");

	}

	public static void startServer(String name) {

		Server server = new ServersView().getServer(name);
		server.start();

		Shell workbenchShell = new WorkbenchShell();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ConsoleHasText("100%"), TimePeriod.getCustom(300));

		// Hack for Mac OS X
		// Returns focus from Apache.karaf.main back to IDE (after starting
		// server)
		workbenchShell.setFocus();
	}

	public static void stopServer(String name) {

		Server server = new ServersView().getServer(name);
		server.stop();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		hack_General_CloseServerTerminateWindow();

	}

	public static List<String> getServers() {

		ServersView serversView = new ServersView();
		serversView.open();
		List<String> temp = new ArrayList<String>();
		try {
			Tree tree = new DefaultTree();
			for (TreeItem item : tree.getItems()) {
				temp.add(new ServerLabel(item).getName());
			}
		} catch (SWTLayerException e) {
			return temp;
		}

		return temp;
	}

	/**
	 * Checks if the Fuse server is started
	 * 
	 * @param name
	 *            Name of the server in Local Processes in JMX Navigator View
	 * @return true - a Fuse server is started, false - otherwise
	 */
	public static boolean isServerStarted(String name) {

		AbstractWait.sleep(TimePeriod.NORMAL);
		FuseJMXNavigator jmx = new FuseJMXNavigator();
		jmx.refresh();
		for (String item : jmx.getLocalProcesses()) {
			if (item.startsWith(name)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the Fuse server with given name exists
	 * 
	 * @param name
	 *            Name of the Server in Servers View
	 * @return true - a Fuse server is present, false - otherwise
	 */
	public static boolean isServerPresent(String name) {

		try {
			new ServersView().getServer(name);
		} catch (EclipseLayerException ex) {
			return false;
		}

		return true;
	}

	/**
	 * If stopping a server takes a long time, <i>Terminate Server</i> window is
	 * appeared. This method tries to close the window.
	 */
	private static void hack_General_CloseServerTerminateWindow() {

		try {
			new DefaultShell("Terminate Server").setFocus();
			new PushButton("OK").click();
		} catch (Exception e) {
			log.info("Window 'Terminate Server' didn't appeared.");
		}
	}
}

package org.jboss.tools.fuse.reddeer.server;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerLabel;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
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

	public static void addServer(String type, String name, String portNumber,
			String userName, String password) {

		ServerWizard serverWizard = new ServerWizard();
		serverWizard.setType(type);
		serverWizard.setName(name);
		serverWizard.setPortNumber(portNumber);
		serverWizard.setUserName(userName);
		serverWizard.setPassword(password);
		serverWizard.execute();

	}

	public static void removeServer(String name) {

		Server server = new ServersView().getServer(name);
		server.delete();

	}

	public static void startServer(String name) {
		
		Server server = new ServersView().getServer(name);
		server.start();
		
		hack_MacOSX_ReturnFocus();
		
	}

	public static void stopServer(String name) {

		Server server = new ServersView().getServer(name);
		server.stop();
		
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
	 * @param name Name of the server in Local Processes in JMX Navigator View
	 * @return true - a Fuse server is started, false - otherwise
	 */
	public static boolean isServerStarted(String name) {
		
		AbstractWait.sleep(5000);
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
	 * Hack for Mac OS X
	 * Returns focus from Apache.karaf.main back to IDE (after starting server)
	 */
	private static void hack_MacOSX_ReturnFocus() {
		
		if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {	
			try {	
				Robot robot = new Robot();
				robot.mouseMove(200, 200);
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
				AbstractWait.sleep(1000);
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			} catch (AWTException e) {
				log.error("Error during click into IDE (OS X)", e);
			}
		}
	}
	
	/**
	 * If stopping a server takes a long time, <i>Terminate Server</i>
	 * window is appeared. This method tries to close the window.
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

package org.jboss.tools.teiid.reddeer.view;

import java.util.Arrays;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.condition.ConsoleHasText;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.list.DefaultList;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.condition.ServerHasState;

public class ServersViewExt extends ServersView2 {

	private static final Logger log = Logger.getLogger(ServersView2.class);
	public static final String DV6_PREFIX_URL = "mm://localhost:9999";
	public static final String EDS5_PREFIX_URL = "mms://localhost:31443";
	private static final String REFRESH = "Refresh / Reconnect Teiid Instance Connection";
	private static final String TEIID_INSTANCE_CONFIG = "Teiid Instance Configuration";
	private static final String DISCONNECT = "Disconnect";
	private static final String DATA_SOURCES = "Data Sources";
	private static final String VDBS = "VDBs";

	public enum ServerType {
		EAP6, EDS5, AS5, DV6
	}

	public ServersViewExt() {
		super();
	}
	
	public static ServersViewExt getInstance(){
		ServersViewExt serversViewExt = new ServersViewExt();
		serversViewExt.open();
		return serversViewExt;
	}

	public boolean isVDBDeployed(String serverName, ServerType serverType, String vdbName) {
		boolean result = false;
		String url = getServerURLPrefix(serverType);
		String[] path = new String[5];
		new ServersView2().getServer(serverName);
		/*
		 * String state = server.getLabel().getState().getText(); String publishState =
		 * server.getLabel().getPublishState().getText();
		 */

		// path[0] = serverName + " [Started, Synchronized]";//or serverName+" [Started]" ---> z LABEL
		// path[0] = serverName + " [" + state + ", " + publishState + "]";
		path[0] = getServerLabel(serverName);
		path[1] = TEIID_INSTANCE_CONFIG;// constant
		path[2] = url + "  [default]";// or url --> redo ala label
		path[3] = "VDBs";
		if (vdbName.contains(".")) {
			path[4] = vdbName.substring(0, vdbName.indexOf("."));
		} else {
			path[4] = vdbName;
		}

		try {
			new DefaultTreeItem(path).select();
			result = true;
		} catch (Exception e) {
			log.error("Couldn't find VDB on path " + path[0] + "/" + path[1] + "/" + path[2] + "/" + path[3] + "/"
					+ path[4]);
			System.err.println(e.getMessage());
		}
		try {
			path[2] = url;
			new DefaultTreeItem(path).select();
			result = true;
		} catch (Exception e) {
			log.error("Couldn't find VDB on path " + path[0] + "/" + path[1] + "/" + path[2] + "/" + path[3] + "/"
					+ path[4]);
			System.err.println(e.getMessage());
		}
		return result;
	}

	public static String getServerURLPrefix(ServerType type) {
		String url = null;
		if (type.equals(ServerType.EAP6) || type.equals(ServerType.DV6)) {
			url = DV6_PREFIX_URL;
		}
		if (type.equals(ServerType.EDS5) || type.equals(ServerType.AS5)) {
			url = EDS5_PREFIX_URL;
		}
		return url;
	}

	public static ServerType determineServerType(String serverName) {// move to serversview ext
		if ((serverName.contains("EAP") || serverName.contains("DV")) && serverName.contains("6")) {
			return ServerType.DV6;
		} else if ((serverName.contains("EAP") || serverName.contains("EDS") || serverName.contains("AS")
				|| serverName.contains("SOA")) && serverName.contains("5")) {
			return ServerType.EDS5;// smazat eap
		}
		return ServerType.DV6;
	}

	public void startServer(String serverName) {
		ServerType type = determineServerType(serverName);
		Server server = new ServersView2().getServer(serverName);
		server.start();
		new WaitUntil(new ServerHasState(serverName), TimePeriod.LONG);
		new WaitUntil(new ConsoleHasText("- Started"), TimePeriod.LONG);

		// additional steps
		if (type.equals(ServerType.EDS5)) {
			connectTeiidInstance(serverName);
		}
		new GuidesView().chooseAction("Teiid", "Refresh ");
		if (new ShellIsActive("Server Selection").test()) { // if you want to disconnect old instance before
																	// switching
			new DefaultCombo().setSelection(serverName);
			new PushButton("OK").click();
		}
		new DefaultShell("Notification");
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell();
	}

	public void restartServer(String serverName) {
		ServerType type = determineServerType(serverName);
		Server server = new ServersView2().getServer(serverName);
		server.restart();
		new WaitUntil(new ServerHasState(serverName), TimePeriod.LONG);
		new WaitUntil(new ConsoleHasText("- Started"), TimePeriod.LONG);

		// additional steps
		if (type.equals(ServerType.EDS5)) {
			connectTeiidInstance(serverName);
		}
		new GuidesView().chooseAction("Teiid", "Refresh ");
		if (new ShellIsActive("Server Selection").test()) { // if you want to disconnect old instance before
																	// switching
			new DefaultCombo().setSelection(serverName);
			new PushButton("OK").click();
		}
		new DefaultShell("Notification");
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell();
	}

	public String getServerLabel(String serverName) {// TEST!!!
		Server server = new ServersView2().getServer(serverName);
		String state = server.getLabel().getState().getText();
		String publishState = server.getLabel().getPublishState().getText();
		if (publishState != null && publishState.length() > 0) {
			return serverName + "  [" + state + ", " + publishState + "]";
		}
		return serverName + "  [" + state + "]";
	}

	public void stopServer(String serverName) {// server.stop?
		Server server = new ServersView2().getServer(serverName);
		server.stop();

		log.info("Stopping server " + server.getLabel().getName());
	}

	public void disconnectTeiidInstance(String serverName) {
		String label = getServerLabel(serverName);
		// refresh

		String url = getServerURLPrefix(determineServerType(serverName));// new DefaultTreeItem(label,
																			// TEIID_INSTANCE_CONFIG, url).expand();
		new DefaultTreeItem(label, TEIID_INSTANCE_CONFIG, url).select();
		refreshServer(serverName);
		new DefaultTreeItem(label, TEIID_INSTANCE_CONFIG, url).select();
		new ContextMenuItem(DISCONNECT).select();
	}

	public void setDefaultTeiidInstance(String serverName) {
		new GuidesView().setDefaultTeiidInstance(serverName);
	}

	public void connectTeiidInstance(String serverName) {
		String label = getServerLabel(serverName);
		// refresh
		new DefaultTreeItem(label, TEIID_INSTANCE_CONFIG).select();
		refreshServer(serverName);
	}

	public void refreshServer(String serverName) {

		String label = getServerLabel(serverName);

		// refresh
		new DefaultTreeItem(label).select();
		activate();
		new DefaultToolItem(REFRESH).click();

		// server was refreshed
		new DefaultShell("Notification");
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell();
	}

	public void createDatasource(String serverName, String connectionProfile, String datasourceName) {
		new ServersView2().getServer(serverName);
		new DefaultTreeItem(getServerLabel(serverName), TEIID_INSTANCE_CONFIG, DV6_PREFIX_URL, DATA_SOURCES).select();
		new ContextMenuItem("Create Data Source").select();
		new DefaultShell("");
		datasourceName = (datasourceName.contains("java:/")) ? datasourceName : "java:/" + datasourceName;
		new DefaultText().setText(datasourceName);
		new RadioButton("Use Connection Profile Info").click();
		new DefaultCombo(1).setSelection(connectionProfile);
		if (!new PushButton("OK").isEnabled()) {
			System.err.println("Datasource " + connectionProfile + "exists!");
			new PushButton("Cancel").click();
		} else {
			new PushButton("OK").click();
		}
	}
	
	public void restartWar(String serverName, String warName){
		new ServersView2().getServer(serverName).getModule(new RegexMatcher(".*" + warName + ".*"));
		new ContextMenuItem("Restart").select();
	}

	public void createDatasource(String serverName, String connectionProfile) {
		createDatasource(serverName, connectionProfile, connectionProfile);
	}

	public void deleteDatasource(String serverName, String dsName) {
		new ServersView2().getServer(serverName);
		try {
			DefaultTreeItem dataSources = new DefaultTreeItem(getServerLabel(serverName), TEIID_INSTANCE_CONFIG,
					DV6_PREFIX_URL, DATA_SOURCES);
			for (TreeItem treeItem : dataSources.getItems()) {
				if (treeItem.getText().startsWith(dsName)) {
					treeItem.select();
					new ContextMenuItem("Delete Data Source").select();
					break;
				}
			}
		} catch (Exception ex) {
			// ds not on server, ok
			ex.printStackTrace();
		}
	}

	public void undeployVdb(String serverName, String vdbName) {
		new ServersView2().getServer(serverName);
		try {
			DefaultTreeItem vdbs = new DefaultTreeItem(getServerLabel(serverName), TEIID_INSTANCE_CONFIG,
					DV6_PREFIX_URL, VDBS);
			for (TreeItem treeItem : vdbs.getItems()) {
				if (treeItem.getText().startsWith(vdbName)) {
					treeItem.select();
					new ContextMenuItem("Undeploy VDB").select();
					break;
				}
			}
		} catch (Exception ex) {
			// vdb not in server
			ex.printStackTrace();
		}
	}

	public String getVdbStatus(String serverName, String vdbName) {
		new ServersView2().getServer(serverName);
		DefaultTreeItem vdbs = new DefaultTreeItem(getServerLabel(serverName), TEIID_INSTANCE_CONFIG, DV6_PREFIX_URL,
				VDBS);
		for (TreeItem treeItem : vdbs.getItems()) {
			if (treeItem.getText().startsWith(vdbName)) {
				treeItem.select();
				try {
					new ContextMenuItem("Show VDB Errors").select();
					String[] errors = new DefaultList(0).getListItems();
					new PushButton("OK").click();
					return "ERROR: " + Arrays.toString(errors);
				} catch (CoreLayerException ex) {
					return "ACTIVE";
				}
			}
		}
		return "ERROR";
	}
}

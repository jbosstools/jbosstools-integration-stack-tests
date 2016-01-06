package org.jboss.tools.teiid.reddeer.manager;

import org.jboss.tools.teiid.reddeer.preference.TeiidDesignerPreferencePage;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt.ServerType;

public class ServerManager {

	/*
	 * public static final int CONFIGURE_NEW_JBOSS_SERVER = 0; public static final int
	 * EDIT_JBOSS_TEIID_INSTANCE_PROPERTIES = 1; public static final int SET_THE_DEFAULT_JBOSS_TEIID_INSTANCE = 2;
	 * public static final int REFRESH_TEIID_INSTANCE = 3;
	 */

	public void startServer(String serverName) {
		new ServersViewExt().startServer(serverName);
	}

	public void stopServer(String serverName) {
		new ServersViewExt().stopServer(serverName);
	}

	/*
	 * public ServersView getServersView(){ return new ServersView(); }
	 */

	public ServersViewExt getServersViewExt() {
		return new ServersViewExt();
	}

	public void setDefaultTeiidInstance(String serverName, ServerType type) {
		new GuidesView().setDefaultTeiidInstance(serverName, type);
	}

	public boolean isVDBDeployed(String serverName, ServerType serverType, String vdbName) {
		return new ServersViewExt().isVDBDeployed(serverName, serverType, vdbName);
	}

	public void editLaunchConfigProgramArgs(String serverName, String appendedText) {
		new ServersViewExt().editLaunchConfigProgramArgs(serverName, appendedText);
	}

	/**
	 * Choose operation:<br />
	 * CONFIGURE_NEW_JBOSS_SERVER,<br />
	 * EDIT_JBOSS_TEIID_INSTANCE_PROPERTIES,<br />
	 * SET_THE_DEFAULT_JBOSS_TEIID_INSTANCE,<br />
	 * REFRESH_TEIID_INSTANCE
	 */
	/*
	 * public void operateTeiidInstanceViaGuidesView(int teiidAction){ //choose operation: switch (teiidAction){ case 0:
	 * System.err.println("Not supported yet"); break; case 1: System.err.println("Not supported yet"); break; case 2:
	 * break; case 3: System.err.println("Not supported yet"); break; } }
	 */

	public void setDefaultTeiidInstanceTargetedVersion(String version) {
		new TeiidDesignerPreferencePage().setDefaultTeiidInstanceTargetedVersion(version);
	}

	public void setTeiidConnectionImporterTimeout(int secs) {
		new TeiidDesignerPreferencePage().setTeiidConnectionImporterTimeout(secs);
	}

}

package org.jboss.tools.teiid.reddeer.manager;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt.ServerType;
import org.jboss.tools.teiid.ui.bot.test.suite.TeiidSuite;

public class ServerManager {
	
	/**
	 * 
	 * @param swtbotFileName as5.properties, dv6.properties
	 */
	public void addServer(String swtbotFileName){
		TeiidSuite.addServerWithProperties(swtbotFileName);
	}
	
	public void startServer(String serverName){
		new ServersViewExt().startServer(serverName);
	}
	
	public void stopServer(String serverName){
		new ServersViewExt().stopServer(serverName);
	}

	public ServersView getServersView(){
		return new ServersView();
	}

	public void setDefaultTeiidInstance(String serverName, ServerType type) {//move to guides view
		new GuidesView().setDefaultTeiidInstance(serverName, type);
	}

	public boolean isVDBDeployed(String serverName, ServerType serverType, String vdbName) {
		return new ServersViewExt().isVDBDeployed(serverName, serverType, vdbName);
	}
	
	public void editLaunchConfigProgramArgs(String serverName, String appendedText){
		new ServersViewExt().editLaunchConfigProgramArgs(serverName, appendedText);
	}
}

package org.jboss.tools.teiid.reddeer.condition;


import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerState;

/**
 * Condition that specifies if a server has state either 'Stopped' or 'Started'
 * 
 * @author apodhrad
 * 
 */
public class ServerHasState extends AbstractWaitCondition {

	protected Logger log = Logger.getLogger(this.getClass());

	private String serverName;
	private ServerState state;

	public ServerHasState(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public boolean test() {
		ServersView2 view = new ServersView2();
		view.open();
		Server server = view.getServer(serverName);
		state = server.getLabel().getState();
		System.out.println("Server's state: " + state);
		return state.equals(ServerState.STARTED) || state.equals(ServerState.STOPPED);
	}

	@Override
	public String description() {
		return "Server has state '" + state + "'";
	}

}

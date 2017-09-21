package org.jboss.tools.teiid.reddeer.condition;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.eclipse.exception.EclipseLayerException;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ModuleLabel;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerPublishState;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerState;

public class WarIsDeployed extends AbstractWaitCondition {

	private Server server;
	private String warName;

	public WarIsDeployed(String serverName, String warName) {
		server = new ServersView2().getServer(serverName);
		this.warName = warName;
	}

	@Override
	public boolean test() {
		try {
			ModuleLabel label = server.getModule(new RegexMatcher(".*" + warName + ".*")).getLabel();
			return ServerPublishState.SYNCHRONIZED.equals(label.getPublishState())
					&& ServerState.STARTED.equals(label.getState());

		} catch (EclipseLayerException ex) {
			return false;
		}
	}

	@Override
	public String description() {
		return "war with name '" + warName + "' is started and synchronized";
	}

}

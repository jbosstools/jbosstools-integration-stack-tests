package org.jboss.tools.teiid.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ModuleLabel;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerPublishState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;

public class WarIsDeployed extends AbstractWaitCondition {

	private Server server;
	private String warName;

	public WarIsDeployed(String serverName, String warName) {
		server = new ServersView().getServer(serverName);
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

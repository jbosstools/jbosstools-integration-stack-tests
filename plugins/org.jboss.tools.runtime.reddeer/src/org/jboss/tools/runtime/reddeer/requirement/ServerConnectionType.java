package org.jboss.tools.runtime.reddeer.requirement;

import org.jboss.tools.runtime.reddeer.ServerBase;

public enum ServerConnectionType {

	LOCAL, REMOTE, ANY;

	public boolean matches(ServerBase serverBase) {
		boolean isRemote = serverBase.isRemote();
		switch (this) {
		case LOCAL:
			return !isRemote;
		case REMOTE:
			return isRemote;
		default:
			return true;
		}
	}

}

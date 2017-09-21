package org.jboss.tools.teiid.reddeer.requirement;

import org.jboss.tools.runtime.reddeer.requirement.ServerConnectionType;
import org.jboss.tools.runtime.reddeer.requirement.ServerImplementationType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRestriction;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;

/**
 * 
 * @author apodhrad
 *
 */
public class TeiidServerRestriction extends ServerRestriction {

	public TeiidServerRestriction() {
		this(ServerConnectionType.LOCAL, ServerImplementationType.AS, ServerImplementationType.EAP);
	}

	public TeiidServerRestriction(ServerImplementationType... implementationTypes) {
		this(ServerConnectionType.LOCAL, implementationTypes);
	}

	public TeiidServerRestriction(ServerConnectionType connectionType,
			ServerImplementationType... implementationTypes) {
		super(TeiidServer.class, "server", connectionType, implementationTypes);
	}

}

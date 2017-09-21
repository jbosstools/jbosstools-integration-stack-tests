package org.jboss.tools.switchyard.reddeer.requirement;

import org.jboss.tools.runtime.reddeer.requirement.ServerImplementationRestriction;
import org.jboss.tools.runtime.reddeer.requirement.ServerImplementationType;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;

/**
 * 
 * @author apodhrad
 *
 */
public class SwitchYardServerRestriction extends ServerImplementationRestriction {

	public SwitchYardServerRestriction(ServerImplementationType... implementationTypes) {
		super(SwitchYard.class, "server", implementationTypes);
	}

}

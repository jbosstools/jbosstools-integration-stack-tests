package org.jboss.tools.runtime.reddeer.requirement;

import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.impl.ServerAS;
import org.jboss.tools.runtime.reddeer.impl.ServerEAP;
import org.jboss.tools.runtime.reddeer.impl.ServerFabric8;
import org.jboss.tools.runtime.reddeer.impl.ServerFuse;
import org.jboss.tools.runtime.reddeer.impl.ServerKaraf;
import org.jboss.tools.runtime.reddeer.impl.ServerServiceMix;
import org.jboss.tools.runtime.reddeer.impl.ServerWildFly;

/**
 * 
 * @author apodhrad
 * 
 */
public enum ServerReqType {

	ANY(null),
	AS(ServerAS.class),
	WildFly(ServerWildFly.class),
	EAP(ServerEAP.class),
	Karaf(ServerKaraf.class),
	Fuse(ServerFuse.class),
	ServiceMix(ServerServiceMix.class),
	Fabric8(ServerFabric8.class);

	private Class<?> clazz;

	private ServerReqType(Class<?> clazz) {
		this.clazz = clazz;
	}

	public boolean matches(ServerBase serverFamily) {
		if (serverFamily == null) {
			return false;
		}
		if (clazz == null) {
			return true;
		}
		return clazz.equals(serverFamily.getClass());
	}
}

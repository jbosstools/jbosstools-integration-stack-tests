package org.jboss.tools.runtime.reddeer.requirement;

import org.eclipse.reddeer.junit.requirement.configuration.RequirementConfiguration;
import org.jboss.tools.runtime.reddeer.RuntimeBase;

/**
 * 
 * @author apodhrad
 * 
 */
public class RuntimeConfiguration implements RequirementConfiguration {

	private String name;
	private RuntimeBase runtime;

	public void setName(String serverName) {
		this.name = serverName;
	}

	public String getName() {
		return name;
	}

	public RuntimeBase getRuntime() {
		return runtime;
	}

	public void setRuntime(RuntimeBase runtime) {
		this.runtime = runtime;
	}

	public RuntimeBase getRuntimeFamily() {
		runtime.setName(name);
		return runtime;
	}

	@Override
	public String getId() {
		if (getName() != null) {
			return getName();
		}
		return getRuntimeFamily().getName();
	}
}

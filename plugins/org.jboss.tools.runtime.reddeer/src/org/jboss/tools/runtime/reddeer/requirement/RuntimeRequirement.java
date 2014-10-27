package org.jboss.tools.runtime.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;

/**
 * 
 * @author apodhrad
 * 
 */

public class RuntimeRequirement implements Requirement<Runtime>, CustomConfiguration<RuntimeConfig> {

	private RuntimeConfig config;
	private Runtime runtime;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Runtime {
		RuntimeReqType type();
	}

	@Override
	public boolean canFulfill() {
		return runtime.type().matches(config.getRuntimeFamily());
	}

	@Override
	public void fulfill() {
		RuntimeBase runtimeFamily = config.getRuntimeFamily();
		if (!runtimeFamily.exists()) {
			runtimeFamily.create();
		}
	}

	@Override
	public void setDeclaration(Runtime server) {
		this.runtime = server;
	}

	@Override
	public Class<RuntimeConfig> getConfigurationClass() {
		return RuntimeConfig.class;
	}

	@Override
	public void setConfiguration(RuntimeConfig config) {
		this.config = config;
	}

	public RuntimeConfig getConfig() {
		return this.config;
	}
}

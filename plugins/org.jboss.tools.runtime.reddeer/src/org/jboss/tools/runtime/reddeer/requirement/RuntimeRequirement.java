package org.jboss.tools.runtime.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.junit.requirement.AbstractConfigurableRequirement;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;

/**
 * 
 * @author apodhrad
 * 
 */

public class RuntimeRequirement extends AbstractConfigurableRequirement<RuntimeConfiguration, Runtime> {

	private static final Logger LOGGER = Logger.getLogger(RuntimeRequirement.class);

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Runtime {

	}

	@Override
	public void fulfill() {
		RuntimeBase runtimeFamily = getConfiguration().getRuntimeFamily();
		if (!runtimeFamily.exists()) {
			LOGGER.info("Creating runtime '" + runtimeFamily.getName() + "'.");
			runtimeFamily.create();
		} else {
			LOGGER.info("Runtime '" + runtimeFamily.getName() + "' already exists.");
		}
	}

	@Override
	public Class<RuntimeConfiguration> getConfigurationClass() {
		return RuntimeConfiguration.class;
	}

	@Override
	public void cleanUp() {
		// we don't want to clean up any runtime
	}
}

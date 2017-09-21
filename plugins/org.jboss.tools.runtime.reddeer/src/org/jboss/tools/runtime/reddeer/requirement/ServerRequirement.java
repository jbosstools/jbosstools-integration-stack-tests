package org.jboss.tools.runtime.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.direct.preferences.Preferences;
import org.eclipse.reddeer.junit.requirement.AbstractConfigurableRequirement;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;

/**
 * 
 * @author apodhrad
 * 
 */

public class ServerRequirement extends AbstractConfigurableRequirement<ServerConfiguration, Server> {

	private static final Logger LOGGER = Logger.getLogger(ServerRequirement.class);

	private ServerConfiguration config;
	private Server server;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Server {
		ServerRequirementState state() default ServerRequirementState.RUNNING;

		String[] property() default "";

		ServerConnectionType[] connectionType() default ServerConnectionType.ANY;

		ServerImplementationType[] implementationType() default ServerImplementationType.ANY;
	}

	@Override
	public void fulfill() {
		ServerBase serverBase = config.getServer();
		List<String> preferences = serverBase.getProperties("preference");
		for (String preference : preferences) {
			// Example: org.eclipse.m2e.core/eclipse.m2.userSettingsFile=settings.xml
			if (preference.matches("([^/=]+)/([^/=]+)=.+")) {
				String[] parts = preference.split("=");
				String key = parts[0];
				String value = parts[1];
				parts = key.split("/");
				String plugin = parts[0];
				String pluginKey = parts[1];
				Preferences.set(plugin, pluginKey, value);
			} else {
				LOGGER.warn("Preference '" + preference + "' doesn't match the patter. SKIPPED");
			}
		}
		if (!serverBase.exists()) {
			serverBase.create();
		}
		serverBase.setState(server.state());
	}

	@Override
	public void setDeclaration(Server server) {
		this.server = server;
	}

	@Override
	public Class<ServerConfiguration> getConfigurationClass() {
		return ServerConfiguration.class;
	}

	@Override
	public void setConfiguration(ServerConfiguration config) {
		this.config = config;
	}

	public ServerConfiguration getConfig() {
		return this.config;
	}

	@Override
	public void cleanUp() {
	}
}

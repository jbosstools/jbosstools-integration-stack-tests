package org.jboss.tools.runtime.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;

/**
 * 
 * @author apodhrad
 * 
 */

public class ServerRequirement implements Requirement<Server>, CustomConfiguration<ServerConfig> {

	private static final Logger LOGGER = Logger.getLogger(ServerRequirement.class);

	private ServerConfig config;
	private Server server;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Server {
		ServerReqType[] type() default ServerReqType.ANY;

		ServerReqState state() default ServerReqState.RUNNING;
	}

	@Override
	public boolean canFulfill() {
		ServerReqType[] type = server.type();
		for (int i = 0; i < type.length; i++) {
			if (type[i].matches(config.getServerBase())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void fulfill() {
		ServerBase serverBase = config.getServerBase();
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
	public Class<ServerConfig> getConfigurationClass() {
		return ServerConfig.class;
	}

	@Override
	public void setConfiguration(ServerConfig config) {
		this.config = config;
	}

	public ServerConfig getConfig() {
		return this.config;
	}
}

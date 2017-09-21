package org.jboss.tools.runtime.reddeer.requirement;

import java.lang.annotation.Annotation;

import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;

/**
 * 
 * @author apodhrad
 *
 */
public class ServerRestriction extends RequirementMatcher {

	public ServerRestriction(ServerConnectionType connectionType, ServerImplementationType... implementationTypes) {
		this(Server.class, "server", connectionType, implementationTypes);
	}

	public ServerRestriction(Class<? extends Annotation> clazz, String attributeName,
			ServerConnectionType connectionType, ServerImplementationType... implementationTypes) {
		super(clazz, attributeName, new ServerMatcher(connectionType, implementationTypes));
	}

	private static class ServerMatcher extends TypeSafeMatcher<ServerBase> {

		private ServerConnectionType connectionType;
		private ServerImplementationType[] implementationTypes;

		public ServerMatcher(ServerConnectionType connectionType, ServerImplementationType... implementationTypes) {
			this.connectionType = connectionType;
			this.implementationTypes = implementationTypes;
		}

		@Override
		public void describeTo(Description description) {

		}

		@Override
		protected boolean matchesSafely(ServerBase serverBase) {
			for (ServerImplementationType implementationType : implementationTypes) {
				if (implementationType.matches(serverBase)) {
					return connectionType.matches(serverBase);
				}
			}
			return false;
		}

	}
}

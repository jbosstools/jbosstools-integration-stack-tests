package org.jboss.tools.runtime.reddeer.requirement;

import java.lang.annotation.Annotation;

import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;

public class ServerConnectionRestriction extends RequirementMatcher {

	public ServerConnectionRestriction(ServerConnectionType connectionType) {
		this(Server.class, "server", connectionType);
	}

	protected ServerConnectionRestriction(Class<? extends Annotation> clazz, String attributeName,
			ServerConnectionType connectionType) {
		super(clazz, attributeName, new ServerConnectionTypeMatcher(connectionType));
	}

	private static class ServerConnectionTypeMatcher extends TypeSafeMatcher<ServerBase> {

		private ServerConnectionType connectionType;

		public ServerConnectionTypeMatcher(ServerConnectionType connectionType) {
			this.connectionType = connectionType;
		}

		@Override
		public void describeTo(Description description) {

		}

		@Override
		protected boolean matchesSafely(ServerBase runtime) {
			return connectionType.matches(runtime);
		}

	}

}

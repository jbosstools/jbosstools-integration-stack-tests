/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
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
public class ServerImplementationRestriction extends RequirementMatcher {

	public ServerImplementationRestriction(ServerImplementationType... implementationTypes) {
		this(Server.class, "server", implementationTypes);
	}

	protected ServerImplementationRestriction(Class<? extends Annotation> clazz, String attributeName,
			ServerImplementationType... implementationTypes) {
		super(clazz, attributeName, new ServerImplementationTypeMatcher(implementationTypes));
	}

	private static class ServerImplementationTypeMatcher extends TypeSafeMatcher<ServerBase> {

		private ServerImplementationType[] implementationTypes;

		public ServerImplementationTypeMatcher(ServerImplementationType... implementationTypes) {
			this.implementationTypes = implementationTypes;
		}

		@Override
		public void describeTo(Description description) {

		}

		@Override
		protected boolean matchesSafely(ServerBase runtime) {
			for (ServerImplementationType implementationType : implementationTypes) {
				if (implementationType.matches(runtime)) {
					return true;
				}
			}
			return false;
		}

	}

}

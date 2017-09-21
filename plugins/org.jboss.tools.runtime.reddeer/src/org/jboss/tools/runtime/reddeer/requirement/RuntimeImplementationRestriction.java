package org.jboss.tools.runtime.reddeer.requirement;

import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;

/**
 * 
 * @author apodhrad
 *
 */
public class RuntimeImplementationRestriction extends RequirementMatcher {

	public RuntimeImplementationRestriction(RuntimeImplementationType... implementationTypes) {
		super(Runtime.class, "runtime", new RuntimeImplementationTypeMatcher(implementationTypes));
	}

	private static class RuntimeImplementationTypeMatcher extends TypeSafeMatcher<RuntimeBase> {

		private RuntimeImplementationType[] implementationTypes;

		public RuntimeImplementationTypeMatcher(RuntimeImplementationType... implementationTypes) {
			this.implementationTypes = implementationTypes;
		}

		@Override
		public void describeTo(Description description) {

		}

		@Override
		protected boolean matchesSafely(RuntimeBase runtime) {
			for (RuntimeImplementationType implementationType : implementationTypes) {
				if (implementationType.matches(runtime)) {
					return true;
				}
			}
			return false;
		}

	}

}

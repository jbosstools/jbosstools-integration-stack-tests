package org.jboss.tools.switchyard.ui.bot.test.condition;

import java.lang.reflect.Field;

import org.eclipse.reddeer.junit.execution.TestMethodShouldRun;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardConfiguration;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.junit.runners.model.FrameworkMethod;

/**
 * 
 * @author apodhrad
 *
 */
public class SwitchYardRequirementHasServer implements TestMethodShouldRun {

	@Override
	public boolean shouldRun(FrameworkMethod method) {
		Class<?> clazz = method.getMethod().getDeclaringClass();
		try {
			Field field = clazz.getDeclaredField("switchyardRequirement");
			field.setAccessible(true);
			Object obj = field.get(SwitchYardRequirement.class);
			if (obj instanceof SwitchYardRequirement) {
				SwitchYardRequirement switchYardRequirement = (SwitchYardRequirement) obj;
				SwitchYardConfiguration config = switchYardRequirement.getConfiguration();
				return config.getServer() != null;
			}
		} catch (Exception e) {
			throw new SwitchYardConditionException("Cannot parse switchyardRequirement.", e);
		}
		return false;
	}

}

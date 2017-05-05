package org.jboss.tools.switchyard.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.tools.switchyard.reddeer.requirement.AutoBuildingRequirement.AutoBuilding;
import org.jboss.tools.switchyard.reddeer.utils.PreferenceUtils;

/**
 * 
 * @author apodhrad
 *
 */
public class AutoBuildingRequirement implements Requirement<AutoBuilding> {

	private AutoBuilding autoBuilding;
	private boolean autoBuildingOriginalValue;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface AutoBuilding {
		boolean value() default true;
	}

	@Override
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {
		autoBuildingOriginalValue = Boolean.valueOf(PreferenceUtils.getAutoBuilding());
		PreferenceUtils.setAutoBuilding(String.valueOf(autoBuilding.value()));
	}

	@Override
	public void setDeclaration(AutoBuilding autoBuilding) {
		this.autoBuilding = autoBuilding;
	}

	@Override
	public void cleanUp() {
		PreferenceUtils.setAutoBuilding(String.valueOf(autoBuildingOriginalValue));
	}
}

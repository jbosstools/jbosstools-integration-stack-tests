package org.jboss.tools.switchyard.reddeer.utils;

import org.jboss.reddeer.direct.preferences.Preferences;

/**
 * 
 * @author apodhrad
 *
 */
public class PreferenceUtils {

	public static String getAutoBuilding() {
		return Preferences.get("org.eclipse.core.resources", "description.autobuilding");
	}

	public static void setAutoBuilding(String value) {
		Preferences.set("org.eclipse.core.resources", "description.autobuilding", value);
	}

	public static boolean isAutoBuildingOn() {
		return "true".equalsIgnoreCase(getAutoBuilding());
	}
}

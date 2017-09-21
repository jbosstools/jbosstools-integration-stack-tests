package org.jboss.tools.common.reddeer.preference;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.direct.preferences.Preferences;

/**
 * 
 * @author Andrej Podhradsky (apodhrad@redhat.com)
 *
 */
public class MavenPreferenceUtil {

	private static final Logger log = Logger.getLogger(MavenPreferenceUtil.class);

	public static final String MAVEN_PLUGIN = "org.eclipse.m2e.core";
	public static final String MAVEN_GLOBAL_SETTINGS_KEY = "eclipse.m2.globalSettingsFile";
	public static final String MAVEN_USER_SETTINGS_KEY = "eclipse.m2.userSettingsFile";

	/**
	 * Returns the maven global settings file
	 * 
	 * @return maven global settings file
	 */
	public static String getMavenGlobalSettingsFile() {
		return Preferences.get(MAVEN_PLUGIN, MAVEN_GLOBAL_SETTINGS_KEY);
	}

	/**
	 * Returns the maven user settings file
	 * 
	 * @return maven user settings file
	 */
	public static String getMavenUserSettingsFile() {
		return Preferences.get(MAVEN_PLUGIN, MAVEN_USER_SETTINGS_KEY);
	}

	/**
	 * Sets the maven global settings file
	 * 
	 */
	public static void setMavenGlobalSettingsFile(String globalSettingsFile) {
		log.info("Sets the maven global settings file to '" + globalSettingsFile + "'");
		Preferences.set(MAVEN_PLUGIN, MAVEN_GLOBAL_SETTINGS_KEY, globalSettingsFile);
	}

	/**
	 * Sets the maven user settings file
	 * 
	 */
	public static void setMavenUserSettingsFile(String userSettingsFile) {
		log.info("Sets the maven global settings file to '" + userSettingsFile + "'");
		Preferences.set(MAVEN_PLUGIN, MAVEN_GLOBAL_SETTINGS_KEY, userSettingsFile);
	}

}

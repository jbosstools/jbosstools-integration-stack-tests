package org.jboss.tools.fuse.reddeer.preference;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.direct.preferences.Preferences;

/**
 * 
 * @author Andrej Podhradsky (apodhrad@redhat.com)
 *
 */
public class StagingRepositoriesPreferenceUtil {

	private static final Logger log = Logger.getLogger(StagingRepositoriesPreferenceUtil.class);

	public static final String STAGING_REPO_PLUGIN = "org.fusesource.ide.projecttemplates";
	public static final String STAGING_REPO_ENABLE_KEY = "enableStagingRepositories";

	/**
	 * Decides whether staging repositories are enabled or not
	 * 
	 * @return true if staging repositories are enabled, false otherwise
	 */
	public static boolean areStagingRepositoriesEnabled() {
		return "true".equals(Preferences.get(STAGING_REPO_PLUGIN, STAGING_REPO_ENABLE_KEY));
	}

	/**
	 * Enables or disables staging repositories
	 */
	public static void setStagingRepositories(boolean enableStagingRepositories) {
		log.info(enableStagingRepositories ? "Enables" : "Disables" + " staging repositories");
		Preferences.set(STAGING_REPO_PLUGIN, STAGING_REPO_ENABLE_KEY, String.valueOf(enableStagingRepositories));
	}

}

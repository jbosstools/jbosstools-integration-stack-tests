package org.jboss.tools.common.reddeer.preference;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.direct.preferences.Preferences;

/**
 * 
 * @author Andrej Podhradsky (apodhrad@redhat.com)
 *
 */
public class SSH2PreferenceUtil {

	private static final Logger log = Logger.getLogger(SSH2PreferenceUtil.class);

	public static final String SSH2_PLUGIN = "org.eclipse.jsch.core";
	public static final String SSH2_HOME_KEY = "SSH2HOME";

	/**
	 * Returns the SSH2 Home
	 * 
	 * @return SSH2 Home
	 */
	public static String getSSH2Home() {
		return Preferences.get(SSH2_PLUGIN, SSH2_HOME_KEY);
	}

	/**
	 * Sets the SSH2 Home
	 * 
	 */
	public static void setSSH2Home(String ssh2Home) {
		log.info("Sets SSH2 Home to '" + ssh2Home + "'");
		Preferences.set(SSH2_PLUGIN, SSH2_HOME_KEY, ssh2Home);
	}

}

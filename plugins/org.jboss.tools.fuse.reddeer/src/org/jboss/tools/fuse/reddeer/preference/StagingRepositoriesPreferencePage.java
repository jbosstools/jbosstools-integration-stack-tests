package org.jboss.tools.fuse.reddeer.preference;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

/**
 * Represents the "Fuse Tooling --> Staging Repositories" preference page
 * 
 * @author tsedmik
 */
public class StagingRepositoriesPreferencePage extends PreferencePage {

	public StagingRepositoriesPreferencePage() {
		super("Fuse Tooling", "Staging Repositories");
	}

	/**
	 * Switch "Enable Staging Repositories" according to the given value
	 * @param value true - turns on staging repositories, false - turns off staging repositories
	 */
	public void toggleStagingRepositories(boolean value) {
		new CheckBox("Enable Staging Repositories").toggle(value);
	}
}
